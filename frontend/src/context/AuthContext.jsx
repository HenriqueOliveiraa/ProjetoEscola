import React, { createContext, useContext, useState, useCallback } from 'react';
import api from '../services/api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem('user');
    return stored ? JSON.parse(stored) : null;
  });

  const login = useCallback(async (username, password) => {
    const response = await api.post('/auth/login', { username, password });
    const { token, username: name, role } = response.data;

    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify({ username: name, role }));
    setUser({ username: name, role });

    return role;
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setUser(null);
  }, []);

  const hasRole = useCallback((roles) => {
    if (!user) return false;
    if (typeof roles === 'string') return user.role === roles;
    return roles.includes(user.role);
  }, [user]);

  const canManage = useCallback(() => {
    return hasRole(['ADMINMAX', 'GESTAO']);
  }, [hasRole]);

  return (
    <AuthContext.Provider value={{ user, login, logout, hasRole, canManage }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within AuthProvider');
  return context;
};
