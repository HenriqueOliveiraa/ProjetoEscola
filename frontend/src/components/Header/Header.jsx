import React from 'react';
import { GraduationCap, Users, BookOpen, School, LogOut } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import './Header.css';

const Header = ({ activeTab, setActiveTab }) => {
  const { user, logout, hasRole } = useAuth();

  const allTabs = [
    { id: 'alunos', label: 'Alunos', icon: Users, roles: ['ADMINMAX', 'GESTAO', 'PROFESSOR'] },
    { id: 'turmas', label: 'Turmas', icon: BookOpen, roles: ['ADMINMAX', 'GESTAO', 'PROFESSOR', 'ALUNO'] },
    { id: 'professores', label: 'Professores', icon: GraduationCap, roles: ['ADMINMAX', 'GESTAO'] }
  ];

  const visibleTabs = allTabs.filter(tab => hasRole(tab.roles));

  return (
    <header className="header">
      <div className="header-content">
        <div className="header-brand">
          <School className="brand-icon" size={36} />
          <div className="brand-text">
            <h1 className="brand-title">EduManager</h1>
            <p className="brand-subtitle">Sistema de Gestão Escolar</p>
          </div>
        </div>

        <nav className="header-nav">
          {visibleTabs.map((tab) => {
            const Icon = tab.icon;
            return (
              <button
                key={tab.id}
                className={`nav-tab ${activeTab === tab.id ? 'active' : ''}`}
                onClick={() => setActiveTab(tab.id)}
              >
                <Icon size={20} />
                <span>{tab.label}</span>
              </button>
            );
          })}
        </nav>

        <div className="header-user">
          <span className="user-info">
            {user?.username} <span className="user-role">({user?.role})</span>
          </span>
          <button className="btn-logout" onClick={logout} title="Sair">
            <LogOut size={20} />
          </button>
        </div>
      </div>
    </header>
  );
};

export default Header;
