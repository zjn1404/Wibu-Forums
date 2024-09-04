import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react'
import { parseJwt } from './auth';

interface AuthContextProps {
  role: string | null;
  setRole: (role: string | null) => void;
}

const AuthContext = createContext<AuthContextProps | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [role, setRole] = useState<string | null>(null);

  useEffect(() => {
    const accessToken = localStorage.getItem('accessToken');
    if (accessToken) {
      const parsedToken = parseJwt(accessToken);
      if (parsedToken && parsedToken.scope) {
        setRole(parsedToken.scope);
      }
    }
  }, []);

  return (
    <AuthContext.Provider value={{ role, setRole }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};