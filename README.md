# Custom Authentication API

Base URL: `http://localhost:8080/api/auth`

---

### POST `/register`

Registers a new user and sends a verification email.

**Request body:**
```json
{
  "email": "user@example.com",
  "password": "secret123"
}
```

| Field | Rule |
|-------|------|
| `email` | Valid email format, cannot be blank |
| `password` | Minimum 6 characters |

**Response** `200 OK`:
```
User registered successfully. Check mail.
```

---

### GET `/verify?token={token}`

Verifies the user's email. Token is sent in the verification email and expires in **5 minutes**.

**Response** `200 OK`:
```
Email verified successfully.
```

---

### POST `/login`

Authenticates a verified user and returns a JWT valid for **24 hours**.

**Request body:**
```json
{
  "email": "user@example.com",
  "password": "secret123"
}
```

**Response** `200 OK`:
```
eyJhbGciOiJIUzI1NiJ9...
```

> If the user is not yet verified, a new verification email is sent automatically.

---

