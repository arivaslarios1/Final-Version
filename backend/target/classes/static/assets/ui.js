/*
  WHAT DOES THIS FILE DO?
  - Prevents auto-scroll on reload (no hash + no restoration)
  - Handles hamburger (mobile) menu open/close + a11y state
  - Smooth-scrolls to sections (contact/demo and arrow button) WITHOUT adding hashes
  - Updates the footer year automatically
  - Drives a scroll-reactive “candy paint” hue shift
  - Reveals elements on scroll with IntersectionObserver
*/

/* ===== STOP RELOAD AUTO-SCROLL (extra guard even though we did it in <head>) ===== */
if ('scrollRestoration' in history) history.scrollRestoration = 'manual';
if (location.hash) {
  history.replaceState(null, '', location.pathname + location.search);
  window.scrollTo(0, 0);
}

/* ===== UTILITIES ===== */
const $  = (sel, root = document) => root.querySelector(sel);
const $$ = (sel, root = document) => Array.from(root.querySelectorAll(sel));

/* ===== HAMBURGER MENU ===== */
function toggleMenu() {
  const menu = $(".menu-links");
  const icon = $(".hamburger-icon");
  const btn  = $("#hamburger-button");
  if (!menu || !icon || !btn) return;

  const isOpen = !icon.classList.contains("open");
  menu.classList.toggle("open", isOpen);
  icon.classList.toggle("open", isOpen);
  btn.setAttribute("aria-expanded", String(isOpen));
}

/* ===== CANDY PAINT HUE SHIFT ===== */
(function hueScroller() {
  const clamp = (n, min, max) => Math.min(max, Math.max(min, n));
  const lerp  = (a, b, t) => a + (b - a) * t;

  function setHueFromScroll() {
    const doc = document.documentElement;
    const maxScroll = doc.scrollHeight - window.innerHeight;
    const t = maxScroll > 0 ? clamp(window.scrollY / maxScroll, 0, 1) : 0;

    const cs     = getComputedStyle(doc);
    const hueMin = parseFloat(cs.getPropertyValue("--hue-min")) || 230;
    const hueMax = parseFloat(cs.getPropertyValue("--hue-max")) || 280;
    const hue    = lerp(hueMin, hueMax, t);

    doc.style.setProperty("--hue", hue + "deg");
  }

  window.addEventListener("load", setHueFromScroll, { once: true });
  window.addEventListener("scroll", setHueFromScroll, { passive: true });
  window.addEventListener("resize", setHueFromScroll);
})();

/* ===== REVEAL ON SCROLL ===== */
function initReveal() {
  const targets = $$(".reveal");
  if (!targets.length) return;

  const io = new IntersectionObserver((entries) => {
    entries.forEach((e) => {
      if (e.isIntersecting) {
        e.target.classList.add("show");
        io.unobserve(e.target);
      }
    });
  }, { threshold: 0.12 });

  targets.forEach((el) => io.observe(el));
}

/* ===== ARROW “NEXT SECTION” ===== */
function initArrowNext() {
  const sections = $$("main > section");
  const arrowBtn = $("button.icon.arrow");
  if (!arrowBtn || sections.length === 0) return;

  arrowBtn.addEventListener("click", () => {
    const y = window.scrollY;
    const buffer = 8;
    const next = sections.find(
      (s) => s.getBoundingClientRect().top + window.scrollY > y + buffer
    ) || sections[0];
    next?.scrollIntoView({ behavior: "smooth", block: "start" });
  });
}

/* ===== SMOOTH SCROLL (NO HASHES) ===== */
function initSmoothJumps() {
  // Intercept ANY #anchor link so URL stays clean
  $$('a[href^="#"]').forEach((a) => {
    a.addEventListener("click", (e) => {
      const id = a.getAttribute("href").slice(1);
      const target = id ? document.getElementById(id) : null;
      if (!target) return; // let browser handle if no target
      e.preventDefault();
      target.scrollIntoView({ behavior: "smooth", block: "start" });
      history.replaceState(null, "", location.pathname + location.search);
    });
  });

  // Explicit “Contact” triggers without hashes
  $$("[data-jump-contact]").forEach((el) =>
    el.addEventListener("click", (e) => {
      e.preventDefault();
      $("#contact")?.scrollIntoView({ behavior: "smooth" });
      history.replaceState(null, "", location.pathname + location.search);
    })
  );
}

/* ===== FOOTER YEAR ===== */
function setFooterYear() {
  const yearEl = $("#year");
  if (yearEl) yearEl.textContent = String(new Date().getFullYear());
}

/* ===== STARTUP ===== */
document.addEventListener("DOMContentLoaded", () => {
  $("#hamburger-button")?.addEventListener("click", toggleMenu);

  $$("[data-close-menu]").forEach((link) =>
    link.addEventListener("click", () => {
      const menu = $(".menu-links");
      const icon = $(".hamburger-icon");
      const btn  = $("#hamburger-button");
      menu?.classList.remove("open");
      icon?.classList.remove("open");
      btn?.setAttribute("aria-expanded", "false");
    })
  );

  initSmoothJumps();
  initArrowNext();
  initReveal();
  setFooterYear();
});
