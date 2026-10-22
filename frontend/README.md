# GigsUp Frontend — no React

HTML, CSS, Tailwind CSS CDN, and vanilla JavaScript. Kept separate from Spring Boot.

## Run locally
1. Start Spring Boot on `http://localhost:8080`.
2. From the repository root run: `python -m http.server 5500 --directory frontend`
3. Open `http://localhost:5500`.

API base defaults to `http://localhost:8080`. To change it, run in the frontend browser console: `localStorage.setItem('gigsup_api_base','http://localhost:8080')`, then reload.

The frontend calls the confirmed auth, posted/accepted shifts, and availability endpoints. Shift matching is intentionally not connected until the exact `ShiftMatchRequest` DTO is checked. OTP response behavior depends on the backend `RegistrationResponse` implementation. Spring Security CORS may need to allow `http://localhost:5500`; this package does not modify backend files.
