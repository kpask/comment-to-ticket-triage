const API_BASE = 'http://localhost:8080';

// Switch between sections
function showSection(id, btn) {
    // Hide all sections
    document.querySelectorAll('.section').forEach(sec => sec.classList.add('hidden'));
    document.getElementById(id).classList.remove('hidden');

    // Update button states
    document.querySelectorAll('.nav-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');

    // Refresh data depending on section
    if (id === 'commentsSection') {
        loadComments();
    } else if (id === 'ticketsSection') {
        loadTickets();
    }
}

async function submitComment() {
    const textarea = document.getElementById('commentText');
    const submitBtn = document.querySelector('.btn-primary');
    if (!textarea || !submitBtn) return;

    const text = textarea.value.trim();
    if (text.length > 255) {
        return showMessage('commentMessage', 'Comment must not exceed 255 characters.', 'error');
    }
    if (!text || text.length < 5) {
        return showMessage('commentMessage', 'Comment must be at least 5 characters.', 'error');
    }

    submitBtn.disabled = true;
    submitBtn.textContent = 'Sending...';
    const originalValue = textarea.value; // Store for rollback if needed
    textarea.value = '';

    try {
        const res = await fetch(`${API_BASE}/comments`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ text })  // <-- use CommentRequest DTO
        });

        if (!res.ok) throw new Error('Failed to submit comment');

        const data = await res.json();
        showMessage('commentMessage', 'Comment submitted!', 'success');
        loadComments();
        loadTickets();

    } catch (err) {
        console.error(err);
        showMessage('commentMessage', 'Error: ' + err.message, 'error');
        textarea.value = originalValue;
    } finally {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Send';
    }
}

async function loadComments() {
    const list = document.getElementById('commentsList');
    const loader = document.getElementById('commentsLoader'); // NEW
    if (!list) return;

    try {
        loader?.classList.remove('hidden'); // NEW
        list.innerHTML = ''; // optional: prevents old content flash

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

        list.innerHTML = comments.slice().reverse().map(c => `
            <div class="feedback-item">
                <div class="feedback-content"><p>${c.text}</p></div>
                <div class="feedback-meta">
                    <small>${new Date(c.createdAt).toLocaleString([], {
            month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit'
        })}</small>
                </div>
            </div>
        `).join('');

    } catch (err) {
        console.error("Load Comments Error:", err);
        list.innerHTML = `
            <div style="color:#991b1b; padding:20px; text-align:center; background:#fee2e2; border-radius:8px;">
                ⚠️ Failed to load recent activity.
            </div>`;
    } finally {
        loader?.classList.add('hidden'); // NEW
    }
}


async function loadTickets() {
    const list = document.getElementById('ticketsList');
    const loader = document.getElementById('ticketsLoader'); // NEW
    if (!list) return;

    try {
        loader?.classList.remove('hidden'); // NEW
        list.innerHTML = '';

        const res = await fetch(`${API_BASE}/tickets`);
        if (!res.ok) throw new Error('Failed to fetch tickets');

        const tickets = await res.json();

        list.innerHTML = tickets.map(t => {
            const priority = t.priority.toLowerCase();
            const originalComment = t.originalComment ? t.originalComment : 'No original comment';

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
                        ${originalComment.replace(/\n/g, '<br>')}
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
    } finally {
        loader?.classList.add('hidden'); // NEW
    }
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
