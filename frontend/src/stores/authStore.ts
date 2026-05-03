import { create } from 'zustand';

interface AuthState {
  token: string | null;
  username: string | null;
  displayName: string | null;
  role: string | null;
  isAuthenticated: boolean;
  login: (token: string, username: string, displayName: string, role: string) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthState>((set) => {
  const stored = localStorage.getItem('auth');
  const initial = stored ? JSON.parse(stored) : {};

  return {
    token: initial.token || null,
    username: initial.username || null,
    displayName: initial.displayName || null,
    role: initial.role || null,
    isAuthenticated: !!initial.token,

    login: (token, username, displayName, role) => {
      const state = { token, username, displayName, role };
      localStorage.setItem('auth', JSON.stringify(state));
      set({ ...state, isAuthenticated: true });
    },

    logout: () => {
      localStorage.removeItem('auth');
      set({ token: null, username: null, displayName: null, role: null, isAuthenticated: false });
    },
  };
});
