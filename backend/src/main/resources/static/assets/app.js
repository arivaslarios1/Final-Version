// assets/app.js
const API_BASE = 'http://localhost:8080'; // change if your backend runs elsewhere

function setToken(t){ localStorage.setItem('token', t); }
function getToken(){ return localStorage.getItem('token'); }
function clearToken(){ localStorage.removeItem('token'); }

async function apiFetch(path, opts = {}) {
  const headers = opts.headers ? {...opts.headers} : {};
  if (!(opts.body instanceof FormData)) {
    headers['Content-Type'] = headers['Content-Type'] || 'application/json';
  }
  const tok = getToken();
  if (tok) headers['Authorization'] = 'Bearer ' + tok;

  const res = await fetch(API_BASE + path, { ...opts, headers });
  if (!res.ok) throw new Error(await res.text().catch(()=>res.statusText));
  const ct = res.headers.get('content-type') || '';
  return ct.includes('application/json') ? res.json() : res.text();
}

// --- login page handler ---
async function handleLoginSubmit(e){
  e.preventDefault();
  const email = document.querySelector('#email').value.trim();
  const password = document.querySelector('#password').value;
  try {
    const data = await apiFetch('/api/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password })
    });
    setToken(data.token);
    location.href = 'app.html';
  } catch (err) {
    alert(err.message || 'Login failed');
  }
}

// --- app page bootstrap ---
async function loadCurrentUser(){
  try {
    const me = await apiFetch('/api/auth/me');
    const who = document.querySelector('#whoami');
    if (who) who.textContent = `${me.name} (${me.role})`;
    document.documentElement.dataset.role = me.role;
  } catch {
    clearToken();
    location.href = 'login.html';
  }
}

function logout(){ clearToken(); location.href = 'login.html'; }

// expose for inline HTML
window.handleLoginSubmit = handleLoginSubmit;
window.loadCurrentUser = loadCurrentUser;
window.logout = logout;


// ... your other functions ...

function showTab(id){
  document.querySelectorAll('.mp-tabpanel').forEach(s => s.classList.remove('active'));
  document.getElementById(id).classList.add('active');
  if(id === 'inventory') loadInventory?.();
  if(id === 'ai') loadAiSuggestions?.();
}
window.showTab = showTab;

// Make Inventory the default visible tab
document.addEventListener('DOMContentLoaded', () => showTab('inventory'));

