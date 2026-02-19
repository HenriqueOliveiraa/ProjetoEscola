import React, { useState, useEffect } from 'react';
import { Plus, Search, Edit2, Trash2, X, Save, GraduationCap, Mail, Phone, Award } from 'lucide-react';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import './PagesStyle.css';

// ── Máscaras ──────────────────────────────────────────────────────────────────
const maskCPF = (value) => {
  const digits = value.replace(/\D/g, '').slice(0, 11);
  if (digits.length <= 3) return digits;
  if (digits.length <= 6) return `${digits.slice(0,3)}.${digits.slice(3)}`;
  if (digits.length <= 9) return `${digits.slice(0,3)}.${digits.slice(3,6)}.${digits.slice(6)}`;
  return `${digits.slice(0,3)}.${digits.slice(3,6)}.${digits.slice(6,9)}-${digits.slice(9)}`;
};

const maskTelefone = (value) => {
  const digits = value.replace(/\D/g, '').slice(0, 11);
  if (digits.length <= 2) return digits.length ? `(${digits}` : '';
  if (digits.length <= 6) return `(${digits.slice(0,2)}) ${digits.slice(2)}`;
  if (digits.length <= 10) return `(${digits.slice(0,2)}) ${digits.slice(2,6)}-${digits.slice(6)}`;
  return `(${digits.slice(0,2)}) ${digits.slice(2,7)}-${digits.slice(7)}`;
};

// ── Validações ─────────────────────────────────────────────────────────────────
const validateCPF = (cpf) => {
  if (!cpf) return null;
  const clean = cpf.replace(/\D/g, '');
  if (clean.length !== 11) return 'CPF deve ter 11 dígitos';
  if (/^(\d)\1{10}$/.test(clean)) return 'CPF inválido';
  let sum = 0;
  for (let i = 0; i < 9; i++) sum += parseInt(clean[i]) * (10 - i);
  let rest = (sum * 10) % 11;
  if (rest === 10 || rest === 11) rest = 0;
  if (rest !== parseInt(clean[9])) return 'CPF inválido';
  sum = 0;
  for (let i = 0; i < 10; i++) sum += parseInt(clean[i]) * (11 - i);
  rest = (sum * 10) % 11;
  if (rest === 10 || rest === 11) rest = 0;
  if (rest !== parseInt(clean[10])) return 'CPF inválido';
  return null;
};

const validateTelefone = (tel) => {
  if (!tel) return null;
  const clean = tel.replace(/\D/g, '');
  if (clean.length < 10 || clean.length > 11) return 'Telefone deve ter 10 ou 11 dígitos';
  return null;
};

// ─────────────────────────────────────────────────────────────────────────────

const ProfessoresPage = () => {
  const { canManage } = useAuth();
  const [professores, setProfessores] = useState([]);
  const [filteredProfessores, setFilteredProfessores] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingProfessor, setEditingProfessor] = useState(null);
  const [fieldErrors, setFieldErrors] = useState({});
  const [formData, setFormData] = useState({
    nome: '',
    sobrenome: '',
    cpf: '',
    idade: '',
    email: '',
    telefone: '',
    disciplina: '',
    registro: ''
  });

  useEffect(() => {
    fetchProfessores();
  }, []);

  useEffect(() => {
    if (searchTerm.trim() === '') {
      setFilteredProfessores(professores);
    } else {
      const lower = searchTerm.toLowerCase();
      const filtered = professores.filter(prof =>
        (prof.nome && prof.nome.toLowerCase().includes(lower)) ||
        (prof.sobrenome && prof.sobrenome.toLowerCase().includes(lower)) ||
        (prof.disciplina && prof.disciplina.toLowerCase().includes(lower)) ||
        (prof.registro && prof.registro.toLowerCase().includes(lower))
      );
      setFilteredProfessores(filtered);
    }
  }, [searchTerm, professores]);

  const fetchProfessores = async () => {
    try {
      setLoading(true);
      const { data } = await api.get('/professores');
      setProfessores(data);
      setFilteredProfessores(data);
    } catch (error) {
      console.error('Erro ao buscar professores:', error);
    } finally {
      setLoading(false);
    }
  };

  const emptyForm = {
    nome: '', sobrenome: '', cpf: '', idade: '',
    email: '', telefone: '', disciplina: '', registro: ''
  };

  const handleOpenModal = (professor = null) => {
    setFieldErrors({});
    if (professor) {
      setEditingProfessor(professor);
      setFormData({
        nome: professor.nome || '',
        sobrenome: professor.sobrenome || '',
        cpf: professor.cpf || '',
        idade: professor.idade || '',
        email: professor.email || '',
        telefone: professor.telefone || '',
        disciplina: professor.disciplina || '',
        registro: professor.registro || ''
      });
    } else {
      setEditingProfessor(null);
      setFormData(emptyForm);
    }
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setEditingProfessor(null);
    setFormData(emptyForm);
    setFieldErrors({});
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;

    // Aplicar máscara
    let maskedValue = value;
    if (name === 'cpf') maskedValue = maskCPF(value);
    if (name === 'telefone') maskedValue = maskTelefone(value);

    setFormData(prev => ({ ...prev, [name]: maskedValue }));

    // Limpar erro do campo ao digitar
    if (fieldErrors[name]) {
      setFieldErrors(prev => ({ ...prev, [name]: null }));
    }
  };

  const validateForm = () => {
    const errors = {};
    const cpfErr = validateCPF(formData.cpf);
    const telErr = validateTelefone(formData.telefone);
    if (cpfErr) errors.cpf = cpfErr;
    if (telErr) errors.telefone = telErr;
    return errors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const errors = validateForm();
    if (Object.keys(errors).length > 0) {
      setFieldErrors(errors);
      return;
    }

    const payload = {
      ...formData,
      idade: parseInt(formData.idade) || 0
    };

    try {
      if (editingProfessor) {
        const { data: updated } = await api.put(`/professores/${editingProfessor.id}`, payload);
        setProfessores(prev => prev.map(p => p.id === updated.id ? updated : p));
      } else {
        const { data: newProfessor } = await api.post('/professores', payload);
        setProfessores(prev => [...prev, newProfessor]);
      }
      handleCloseModal();
    } catch (error) {
      if (error.response?.data && typeof error.response.data === 'object') {
        setFieldErrors(error.response.data);
      } else {
        alert('Erro ao salvar. Verifique os dados e tente novamente.');
      }
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Tem certeza que deseja excluir este professor?')) return;
    try {
      await api.delete(`/professores/${id}`);
      setProfessores(prev => prev.filter(p => p.id !== id));
    } catch (error) {
      console.error('Erro ao deletar professor:', error);
      alert('Erro ao excluir professor.');
    }
  };

  if (loading) {
    return (
      <div className="page-container">
        <div className="spinner"></div>
      </div>
    );
  }

  return (
    <div className="page-container">
      <div className="page-header">
        <div className="page-title-section">
          <h1 className="page-title">Gestão de Professores</h1>
          <p className="page-subtitle">Gerencie o corpo docente da instituição</p>
        </div>
        {canManage() && (
          <button className="btn btn-primary" onClick={() => handleOpenModal()}>
            <Plus size={20} />
            Novo Professor
          </button>
        )}
      </div>

      <div className="search-container">
        <Search className="search-icon" size={20} />
        <input
          type="text"
          className="search-input"
          placeholder="Buscar por nome, disciplina ou registro..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
      </div>

      {filteredProfessores.length === 0 ? (
        <div className="empty-state">
          <GraduationCap className="empty-state-icon" size={80} />
          <h3>Nenhum professor encontrado</h3>
          <p>Comece adicionando um novo professor ao sistema</p>
        </div>
      ) : (
        <div className="table-container">
          <table className="table">
            <thead>
              <tr>
                <th>Registro</th>
                <th>Nome</th>
                <th>Sobrenome</th>
                <th>CPF</th>
                <th>Email</th>
                <th>Telefone</th>
                <th>Disciplina</th>
                <th>Turmas</th>
                {canManage() && <th>Ações</th>}
              </tr>
            </thead>
            <tbody>
              {filteredProfessores.map(professor => (
                <tr key={professor.id}>
                  <td><span className="badge badge-primary">{professor.registro || '-'}</span></td>
                  <td><strong>{professor.nome}</strong></td>
                  <td>{professor.sobrenome || '-'}</td>
                  <td>{professor.cpf || '-'}</td>
                  <td>{professor.email || '-'}</td>
                  <td>{professor.telefone || '-'}</td>
                  <td>
                    {professor.disciplina && (
                      <span className="badge badge-success">
                        <Award size={14} /> {professor.disciplina}
                      </span>
                    )}
                  </td>
                  <td>
                    {professor.turmas && professor.turmas.length > 0
                      ? professor.turmas.map(t => t.nome).join(', ')
                      : '-'}
                  </td>
                  {canManage() && (
                    <td>
                      <div className="action-buttons">
                        <button className="btn btn-icon btn-secondary" onClick={() => handleOpenModal(professor)} title="Editar">
                          <Edit2 size={16} />
                        </button>
                        <button className="btn btn-icon btn-danger" onClick={() => handleDelete(professor.id)} title="Excluir">
                          <Trash2 size={16} />
                        </button>
                      </div>
                    </td>
                  )}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {showModal && (
        <div className="modal-overlay" onClick={handleCloseModal}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2>{editingProfessor ? 'Editar Professor' : 'Novo Professor'}</h2>
              <button className="modal-close" onClick={handleCloseModal}><X size={24} /></button>
            </div>
            <form onSubmit={handleSubmit}>
              <div className="modal-body">

                <div className="form-group">
                  <label className="form-label"><GraduationCap size={16} /> Nome</label>
                  <input type="text" name="nome" className="form-input" value={formData.nome} onChange={handleInputChange} required placeholder="Nome" />
                </div>

                <div className="form-group">
                  <label className="form-label">Sobrenome</label>
                  <input type="text" name="sobrenome" className="form-input" value={formData.sobrenome} onChange={handleInputChange} placeholder="Sobrenome" />
                </div>

                <div className="form-group">
                  <label className="form-label">CPF</label>
                  <input
                    type="text"
                    name="cpf"
                    className={`form-input ${fieldErrors.cpf ? 'input-error' : ''}`}
                    value={formData.cpf}
                    onChange={handleInputChange}
                    placeholder="000.000.000-00"
                    maxLength={14}
                  />
                  {fieldErrors.cpf && <span className="field-error">{fieldErrors.cpf}</span>}
                </div>

                <div className="form-group">
                  <label className="form-label">Idade</label>
                  <input type="number" name="idade" className="form-input" value={formData.idade} onChange={handleInputChange} placeholder="Idade" min="0" max="120" />
                </div>

                <div className="form-group">
                  <label className="form-label"><Mail size={16} /> Email</label>
                  <input type="email" name="email" className="form-input" value={formData.email} onChange={handleInputChange} placeholder="email@escola.com" />
                </div>

                <div className="form-group">
                  <label className="form-label"><Phone size={16} /> Telefone</label>
                  <input
                    type="text"
                    name="telefone"
                    className={`form-input ${fieldErrors.telefone ? 'input-error' : ''}`}
                    value={formData.telefone}
                    onChange={handleInputChange}
                    placeholder="(00) 00000-0000"
                    maxLength={15}
                  />
                  {fieldErrors.telefone && <span className="field-error">{fieldErrors.telefone}</span>}
                </div>

                <div className="form-group">
                  <label className="form-label"><Award size={16} /> Disciplina</label>
                  <input type="text" name="disciplina" className="form-input" value={formData.disciplina} onChange={handleInputChange} required placeholder="Ex: Matemática, Português..." />
                </div>

                <div className="form-group">
                  <label className="form-label">Registro Profissional</label>
                  <input type="text" name="registro" className="form-input" value={formData.registro} onChange={handleInputChange} placeholder="PROF2024001" />
                </div>

              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={handleCloseModal}>
                  <X size={16} /> Cancelar
                </button>
                <button type="submit" className="btn btn-primary">
                  <Save size={16} /> {editingProfessor ? 'Atualizar' : 'Cadastrar'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default ProfessoresPage;
