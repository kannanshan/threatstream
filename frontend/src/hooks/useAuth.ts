import { useAuthStore } from '../stores/authStore';

export function useAuth() {
  const { isAuthenticated, username, displayName, role, login, logout } = useAuthStore();
  return { isAuthenticated, username, displayName, role, login, logout };
}
