# Frontend Architecture

## Tech Stack
- **React 19** + **TypeScript**
- **Tailwind CSS** for styling
- **React Router v7** for routing
- **Zustand** for state management
- **Axios** for HTTP requests
- **Vite** as build tool

## Project Structure

```
frontend/src/
├── api/
│   └── client.ts           # Axios instance with interceptors
├── components/
│   ├── common/             # Reusable UI components
│   │   ├── LoadingSpinner.tsx
│   │   ├── ErrorAlert.tsx
│   │   ├── OAuthButtons.tsx
│   │   └── FormInput.tsx
│   ├── Header.tsx          # Navigation bar
│   ├── Layout.tsx          # Page layout wrapper
│   ├── ProtectedRoute.tsx  # Auth guard for routes
│   ├── PostCard.tsx        # Post list item
│   ├── CommentItem.tsx     # Comment display/edit
│   └── Pagination.tsx      # Pagination controls
├── pages/
│   ├── Board.tsx           # Post list (/)
│   ├── Login.tsx           # Login (/login)
│   ├── Signup.tsx          # Registration (/signup)
│   ├── PostDetail.tsx      # Post view (/post/:id)
│   ├── PostWrite.tsx       # Create/edit (/post/write, /post/edit/:id)
│   ├── MyPage.tsx          # User profile (/mypage)
│   └── OAuthCallback.tsx   # OAuth redirect handler
├── stores/
│   └── authStore.ts        # Auth state (Zustand)
├── utils/
│   ├── formatDate.ts       # Date formatting utilities
│   └── tokenManager.ts     # Token localStorage operations
├── types/
│   └── index.ts            # TypeScript interfaces
├── App.tsx                 # Router configuration
├── main.tsx                # Entry point
└── index.css               # Tailwind imports
```

## Routes

| Path | Component | Auth Required | Description |
|------|-----------|---------------|-------------|
| `/` | Board | No | Post list |
| `/login` | Login | No | Login form |
| `/signup` | Signup | No | Registration form |
| `/post/:id` | PostDetail | No | View post |
| `/post/write` | PostWrite | Yes | Create post |
| `/post/edit/:id` | PostWrite | Yes | Edit post |
| `/mypage` | MyPage | Yes | User profile |
| `/oauth/callback` | OAuthCallback | No | OAuth redirect |

## State Management

### Auth Store (Zustand)
```typescript
interface AuthState {
  isAuthenticated: boolean;
  user: UserInfo | null;
  isLoading: boolean;
  login: (data) => Promise<void>;
  logout: () => void;
  initialize: () => Promise<void>;
}
```

### Token Management
- Access token: Short-lived JWT for API requests
- Refresh token: Long-lived token for renewal
- Storage: localStorage via `tokenManager` utility
- Auto-refresh: 401 interceptor handles token renewal

## API Integration

### Axios Instance
- Base URL: `/api` (proxied to backend in dev)
- Request interceptor: Adds `Authorization` header
- Response interceptor: Auto-refresh on 401

### API Modules
- `authApi`: Login, signup, token reissue
- `userApi`: User profile operations
- `postApi`: CRUD for posts
- `commentApi`: CRUD for comments

## Commands

```bash
# Install dependencies
npm install

# Development server
npm run dev

# Production build
npm run build

# Lint
npm run lint
```

## Development

### Proxy Configuration (vite.config.ts)
```typescript
server: {
  proxy: {
    '/api': 'http://localhost:8080'
  }
}
```
