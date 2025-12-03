// frontend/src/App.jsx
import "./App.css";

function App() {
  return (
    <div className="app" style={{ textAlign: "center", marginTop: "4rem" }}>
      <h1>ManagePro Inventory System</h1>
      <p>Full-stack CSC-481 final project</p>

      <div style={{ marginTop: "2rem" }}>
        <h2>Backend status</h2>
        <p>
          Spring Boot server is running at <code>http://localhost:8081</code>.
        </p>
        <p>
          Try opening <code>http://localhost:8081/api/categories</code> or{" "}
          <code>http://localhost:8081/api/products</code> in your browser to
          see JSON from the backend.
        </p>
      </div>

      <div style={{ marginTop: "2rem" }}>
        <h2>About this app</h2>
        <ul style={{ display: "inline-block", textAlign: "left" }}>
          <li>Java 17 + Spring Boot 3 (REST API)</li>
          <li>H2 in-memory database with JPA entities</li>
          <li>React + Vite frontend bundled into Spring Boot</li>
          <li>
            JWT-based authentication endpoints under <code>/api/auth</code>
          </li>
        </ul>
      </div>
    </div>
  );
}

export default App;