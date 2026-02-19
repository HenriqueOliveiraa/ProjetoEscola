import React from 'react';
import { Heart } from 'lucide-react';
import './Footer.css';

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-content">
        <p className="footer-text">
          Feito com dedicação para educação
        </p>
        <p className="footer-copy">
          © {new Date().getFullYear()} EduManager - Sistema de Gestão Escolar
        </p>
      </div>
    </footer>
  );
};

export default Footer;
