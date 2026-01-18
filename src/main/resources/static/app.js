const API_BASE = 'http://localhost:8080';

// Switch between sections
function showSection(id, btn) {
    // Hide all sections
    document.querySelectorAll('.section').forEach(sec => sec.classList.add('hidden'));
    document.getElementById(id).classList.remove('hidden');

    // Update button states
    document.querySelectorAll('.nav-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
}

// Submit a comment
// Submit a comment
async function submitComment() {
    const textarea = document.getElementById('commentText');
    const submitBtn = document.querySelector('.btn-primary');

    if (!textarea || !submitBtn) return;

    const text = textarea.value.trim();

    // Improved validation: Empty + Length check
    if (!text || text.length < 5) {
        return showMessage('commentMessage', 'Comment must be at least 5 characters.', 'error');
    }

    // 1. UI State: Disable & Clear
    submitBtn.disabled = true;
    submitBtn.textContent = 'Sending...';
    const originalValue = textarea.value; // Store for rollback if needed
    textarea.value = '';

    try {
        const res = await fetch(`${API_BASE}/comments`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: text
        });

        if (!res.ok) throw new Error('Failed to submit comment');

        const data = await res.json();

        showMessage('commentMessage', data.createTicket ? 'Ticket created!' : 'Comment submitted', 'success');

        // Refresh lists
        loadComments();
        loadTickets();

    } catch (err) {
        console.error(err);
        showMessage('commentMessage', 'Error: ' + err.message, 'error');
        // Rollback text if it failed so user doesn't lose their draft
        textarea.value = originalValue;
    } finally {
        // 2. UI State: Re-enable
        submitBtn.disabled = false;
        submitBtn.textContent = 'Send';
    }
}

// Load all comments
async function loadComments() {
    const list = document.getElementById('commentsList');
    if (!list) return;

    try {
        const res = await fetch(`${API_BASE}/comments`);
        if (!res.ok) throw new Error('Failed to fetch');

        const comments = await res.json();

        if (!comments || comments.length === 0) {
            list.innerHTML = `
                <div style="text-align:center; padding:40px; color:var(--text-muted); border:2px dashed var(--border); border-radius:12px;">
                    No feedback activity yet.
                </div>`;
            return;
        }

        list.innerHTML = comments.slice().reverse().map(c => {
            let cleanText = c.text;

            // Try parsing JSON content if it looks like JSON
            if (typeof cleanText === 'string' && cleanText.trim().startsWith('{')) {
                try {
                    const parsed = JSON.parse(cleanText);
                    cleanText = parsed.text || parsed.content || cleanText;
                } catch (e) {
                    console.warn("Could not parse comment JSON content", e);
                }
            }

            return `
                <div class="feedback-item">
                    <div class="feedback-content">
                        <p>${cleanText}</p>
                    </div>
                    <div class="feedback-meta">
                        <small>${new Date(c.createdAt).toLocaleString([], {
                month: 'short',
                day: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            })}</small>
                    </div>
                </div>
            `;
        }).join('');

    } catch (err) {
        console.error("Load Comments Error:", err);
        list.innerHTML = `
            <div style="color:#991b1b; padding:20px; text-align:center; background:#fee2e2; border-radius:8px;">
                ⚠️ Failed to load recent activity.
            </div>`;
    }
}

async function loadTickets() {
    const list = document.getElementById('ticketsList');
    if (!list) return;

    try {
        const res = await fetch(`${API_BASE}/tickets`);
        const tickets = await res.json();

        list.innerHTML = tickets.map(t => {
            const priority = t.priority.toLowerCase();

            // --- CLEANING LOGIC START ---
            let originalComment = t.comment?.text || "No original comment";

            // Handle Java toString() format: {text=value} or {text:value}
            if (typeof originalComment === 'string') {
                // Remove Java toString() curly braces and 'text=' prefix
                // This converts "{text=Hello}" into "Hello"
                originalComment = originalComment.replace(/^{text[=:]\s?/, '').replace(/}$/, '');

                // If it's still JSON formatted, try to parse it
                if (originalComment.startsWith('{')) {
                    try {
                        const parsed = JSON.parse(originalComment);
                        originalComment = parsed.text || originalComment;
                    } catch (e) { /* Fallback to raw string */ }
                }
            }
            // --- CLEANING LOGIC END ---

            return `
                <div class="ticket">
                    <div style="display:flex; justify-content: space-between; align-items: start;">
                        <span class="badge ${priority}">${t.priority}</span>
                        <small style="color:var(--text-muted)">${t.category}</small>
                    </div>
                    <h3 style="margin: 10px 0 5px 0">${t.title}</h3>
                    <p style="color: var(--text-muted); font-size: 0.9rem;">${t.summary}</p>

                    <button class="btn-show-comment" onclick="this.nextElementSibling.classList.toggle('hidden')">
                        Show Original Comment
                    </button>
                    <div class="original-comment hidden">
                        ${originalComment.trim().replace(/\n/g, '<br>')}
                    </div>
                    <hr style="border:0; border-top:1px solid var(--border); margin: 15px 0;">
                    <div style="font-size: 0.8rem; font-weight: bold; color: var(--primary)">
                        Status: ${t.status}
                    </div>
                </div>
            `;
        }).join('');

    } catch (err) {
        console.error("Load Tickets Error:", err);
        list.innerHTML = '<p>Failed to load tickets</p>';
    }
}


// Toggle original comment for a ticket
function toggleOriginalComment(id) {
    const el = document.getElementById(`orig-${id}`);
    if (el) el.classList.toggle('hidden');
}

// Show temporary message
function showMessage(id, msg, type) {
    const el = document.getElementById(id);
    if (!el) return;
    el.textContent = msg;
    el.className = `message ${type}`;
    el.classList.remove('hidden');
    setTimeout(() => el.classList.add('hidden'), 5000);
}

// Load both lists on page load
window.onload = () => {
    loadComments();
    loadTickets();
};
