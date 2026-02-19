import React, { useState, useEffect } from 'react';
import { Plus, Search, Edit2, Trash2, X, Save, Mail, Phone, Calendar, User } from 'lucide-react';
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

const validateDataNascimento = (date) => {
  if (!date) return null;
  if (new Date(date) >= new Date()) return 'Data de nascimento deve ser no passado';
  return null;
};

// ─────────────────────────────────────────────────────────────────────────────

const AlunosPage = () => {
  const { canManage } = useAuth();
  const [alunos, setAlunos] = useState([]);
  const [filteredAlunos, setFilteredAlunos] = useState([]);
  const [turmas, setTurmas] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingAluno, setEditingAluno] = useState(null);
  const [fieldErrors, setFieldErrors] = useState({});
  const [formData, setFormData] = useState({
    nome: '',
    sobrenome: '',
    cpf: '',
    idade: '',
    email: '',
    telefone: '',
    dataNascimento: '',
    matricula: '',
    turmaId: ''
  });

  useEffect(() => {
    fetchAlunos();
    fetchTurmas();
  }, []);

  useEffect(() => {
    if (searchTerm.trim() === '') {
      setFilteredAlunos(alunos);
    } else {
      const lower = searchTerm.toLowerCase();
      const filtered = alunos.filter(aluno =>
        (aluno.nome && aluno.nome.toLowerCase().includes(lower)) ||
        (aluno.sobrenome && aluno.sobrenome.toLowerCase().includes(lower)) ||
        (aluno.cpf && aluno.cpf.includes(searchTerm)) ||
        (aluno.matricula && aluno.matricula.toLowerCase().includes(lower))
      );
      setFilteredAlunos(filtered);
    }
  }, [searchTerm, alunos]);

  const fetchAlunos = async () => {
    try {
      setLoading(true);
      const { data } = await api.get('/alunos');
      setAlunos(data);
      setFilteredAlunos(data);
    } catch (error) {
      console.error('Erro ao buscar alunos:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchTurmas = async () => {
    try {
      const { data } = await api.get('/turmas');
      setTurmas(data);
    } catch (error) {
      console.error('Erro ao buscar turmas:', error);
    }
  };

  const emptyForm = {
    nome: '', sobrenome: '', cpf: '', idade: '', email: '',
    telefone: '', dataNascimento: '', matricula: '', turmaId: ''
  };

  const handleOpenModal = (aluno = null) => {
    setFieldErrors({});
    if (aluno) {
      setEditingAluno(aluno);
      setFormData({
        nome: aluno.nome || '',
        sobrenome: aluno.sobrenome || '',
        cpf: aluno.cpf || '',
        idade: aluno.idade || '',
        email: aluno.email || '',
        telefone: aluno.telefone || '',
        dataNascimento: aluno.dataNascimento || '',
        matricula: aluno.matricula || '',
        turmaId: aluno.turmaId || ''
      });
    } else {
      setEditingAluno(null);
      setFormData(emptyForm);
    }
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setEditingAluno(null);
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
    const dateErr = validateDataNascimento(formData.dataNascimento);
    if (cpfErr) errors.cpf = cpfErr;
    if (telErr) errors.telefone = telErr;
    if (dateErr) errors.dataNascimento = dateErr;
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
      idade: parseInt(formData.idade) || 0,
      turmaId: formData.turmaId ? parseInt(formData.turmaId) : null,
      dataNascimento: formData.dataNascimento || null
    };

    try {
      if (editingAluno) {
        const { data: updated } = await api.put(`/alunos/${editingAluno.id}`, payload);
        setAlunos(prev => prev.map(a => a.id === updated.id ? updated : a));
      } else {
        const { data: newAluno } = await api.post('/alunos', payload);
        setAlunos(prev => [...prev, newAluno]);
      }
      handleCloseModal();
    } catch (error) {
      if (error.response?.data && typeof error.response.data === 'object') {
        // Erros de validação do backend (campo: mensagem)
        setFieldErrors(error.response.data);
      } else {
        alert('Erro ao salvar. Verifique os dados e tente novamente.');
      }
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Tem certeza que deseja excluir este aluno?')) return;
    try {
      await api.delete(`/alunos/${id}`);
      setAlunos(prev => prev.filter(a => a.id !== id));
    } catch (error) {
      console.error('Erro ao deletar aluno:', error);
      alert('Erro ao excluir aluno.');
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return '-';
    return new Date(dateString + 'T12:00:00').toLocaleDateString('pt-BR');
  };

  const today = new Date().toISOString().split('T')[0];

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
          <h1 className="page-title">Gestão de Alunos</h1>
          <p className="page-subtitle">Gerencie todos os alunos da instituição</p>
        </div>
        {canManage() && (
          <button className="btn btn-primary" onClick={() => handleOpenModal()}>
            <Plus size={20} />
            Novo Aluno
          </button>
        )}
      </div>

      <div className="search-container">
        <Search className="search-icon" size={20} />
        <input
          type="text"
          className="search-input"
          placeholder="Buscar por nome, CPF ou matrícula..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
      </div>

      {filteredAlunos.length === 0 ? (
        <div className="empty-state">
          <User className="empty-state-icon" size={80} />
          <h3>Nenhum aluno encontrado</h3>
          <p>Comece adicionando um novo aluno ao sistema</p>
        </div>
      ) : (
        <div className="table-container">
          <table className="table">
            <thead>
              <tr>
                <th>Matrícula</th>
                <th>Nome</th>
                <th>Sobrenome</th>
                <th>CPF</th>
                <th>Email</th>
                <th>Telefone</th>
                <th>Nasc.</th>
                <th>Turma</th>
                {canManage() && <th>Ações</th>}
              </tr>
            </thead>
            <tbody>
              {filteredAlunos.map(aluno => (
                <tr key={aluno.id}>
                  <td><span className="badge badge-primary">{aluno.matricula || '-'}</span></td>
                  <td><strong>{aluno.nome}</strong></td>
                  <td>{aluno.sobrenome || '-'}</td>
                  <td>{aluno.cpf || '-'}</td>
                  <td>{aluno.email || '-'}</td>
                  <td>{aluno.telefone || '-'}</td>
                  <td>{formatDate(aluno.dataNascimento)}</td>
                  <td>{aluno.turmaNome || '-'}</td>
                  {canManage() && (
                    <td>
                      <div className="action-buttons">
                        <button className="btn btn-icon btn-secondary" onClick={() => handleOpenModal(aluno)} title="Editar">
                          <Edit2 size={16} />
                        </button>
                        <button className="btn btn-icon btn-danger" onClick={() => handleDelete(aluno.id)} title="Excluir">
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
              <h2>{editingAluno ? 'Editar Aluno' : 'Novo Aluno'}</h2>
              <button className="modal-close" onClick={handleCloseModal}><X size={24} /></button>
            </div>
            <form onSubmit={handleSubmit}>
              <div className="modal-body">

                <div className="form-group">
                  <label className="form-label"><User size={16} /> Nome</label>
                  <input type="text" name="nome" className="form-input" value={formData.nome} onChange={handleInputChange} required placeholder="Nome" />
                </div>

                <div className="form-group">
                  <label className="form-label"><User size={16} /> Sobrenome</label>
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
                  <input type="email" name="email" className="form-input" value={formData.email} onChange={handleInputChange} placeholder="email@exemplo.com" />
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
                  <label className="form-label"><Calendar size={16} /> Data de Nascimento</label>
                  <input
                    type="date"
                    name="dataNascimento"
                    className={`form-input ${fieldErrors.dataNascimento ? 'input-error' : ''}`}
                    value={formData.dataNascimento}
                    onChange={handleInputChange}
                    max={today}
                  />
                  {fieldErrors.dataNascimento && <span className="field-error">{fieldErrors.dataNascimento}</span>}
                </div>

                <div className="form-group">
                  <label className="form-label">Matrícula</label>
                  <input type="text" name="matricula" className="form-input" value={formData.matricula} onChange={handleInputChange} placeholder="ALU2024001" />
                </div>

                <div className="form-group">
                  <label className="form-label">Turma</label>
                  <select name="turmaId" className="form-select" value={formData.turmaId} onChange={handleInputChange}>
                    <option value="">Selecione uma turma</option>
                    {turmas.map(t => (
                      <option key={t.id} value={t.id}>{t.nome}</option>
                    ))}
                  </select>
                </div>

              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={handleCloseModal}>
                  <X size={16} /> Cancelar
                </button>
                <button type="submit" className="btn btn-primary">
                  <Save size={16} /> {editingAluno ? 'Atualizar' : 'Cadastrar'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default AlunosPage;
