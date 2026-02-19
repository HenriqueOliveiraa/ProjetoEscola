import React, { useState, useEffect } from 'react';
import { Plus, Search, Edit2, Trash2, X, Save, BookOpen, Users, Clock } from 'lucide-react';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import './PagesStyle.css';

const TurmasPage = () => {
  const { canManage } = useAuth();
  const [turmas, setTurmas] = useState([]);
  const [filteredTurmas, setFilteredTurmas] = useState([]);
  const [professores, setProfessores] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingTurma, setEditingTurma] = useState(null);
  const [formData, setFormData] = useState({
    nome: '',
    ano: new Date().getFullYear().toString(),
    periodo: 'Manhã',
    capacidadeMaxima: '',
    professoresIds: []
  });

  useEffect(() => {
    fetchTurmas();
    fetchProfessores();
  }, []);

  useEffect(() => {
    if (searchTerm.trim() === '') {
      setFilteredTurmas(turmas);
    } else {
      const lower = searchTerm.toLowerCase();
      const filtered = turmas.filter(turma =>
        (turma.nome && turma.nome.toLowerCase().includes(lower)) ||
        (turma.ano && turma.ano.toString().includes(searchTerm)) ||
        (turma.periodo && turma.periodo.toLowerCase().includes(lower))
      );
      setFilteredTurmas(filtered);
    }
  }, [searchTerm, turmas]);

  const fetchTurmas = async () => {
    try {
      setLoading(true);
      const { data } = await api.get('/turmas');
      setTurmas(data);
      setFilteredTurmas(data);
    } catch (error) {
      console.error('Erro ao buscar turmas:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchProfessores = async () => {
    try {
      const { data } = await api.get('/professores');
      setProfessores(data);
    } catch (error) {
      console.error('Erro ao buscar professores:', error);
    }
  };

  const emptyForm = {
    nome: '',
    ano: new Date().getFullYear().toString(),
    periodo: 'Manhã',
    capacidadeMaxima: '',
    professoresIds: []
  };

  const handleOpenModal = (turma = null) => {
    if (turma) {
      setEditingTurma(turma);
      setFormData({
        nome: turma.nome || '',
        ano: turma.ano || new Date().getFullYear().toString(),
        periodo: turma.periodo || 'Manhã',
        capacidadeMaxima: turma.capacidadeMaxima || '',
        professoresIds: []
      });
    } else {
      setEditingTurma(null);
      setFormData(emptyForm);
    }
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setEditingTurma(null);
    setFormData(emptyForm);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleProfessoresChange = (e) => {
    const selected = Array.from(e.target.selectedOptions, opt => parseInt(opt.value));
    setFormData(prev => ({ ...prev, professoresIds: selected }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const payload = {
      ...formData,
      capacidadeMaxima: formData.capacidadeMaxima ? parseInt(formData.capacidadeMaxima) : null
    };

    try {
      if (editingTurma) {
        const { data: updated } = await api.put(`/turmas/${editingTurma.id}`, payload);
        setTurmas(prev => prev.map(t => t.id === updated.id ? updated : t));
      } else {
        const { data: newTurma } = await api.post('/turmas', payload);
        setTurmas(prev => [...prev, newTurma]);
      }
      handleCloseModal();
    } catch (error) {
      console.error('Erro ao salvar turma:', error);
      alert('Erro ao salvar. Verifique os dados e tente novamente.');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Tem certeza que deseja excluir esta turma?')) return;
    try {
      await api.delete(`/turmas/${id}`);
      setTurmas(prev => prev.filter(t => t.id !== id));
    } catch (error) {
      console.error('Erro ao deletar turma:', error);
      alert('Erro ao excluir turma.');
    }
  };

  const getCapacidadeBadge = (turma) => {
    if (!turma.capacidadeMaxima) return 'badge-primary';
    const percentual = (turma.alunosCount / turma.capacidadeMaxima) * 100;
    if (percentual >= 90) return 'badge-danger';
    if (percentual >= 70) return 'badge-warning';
    return 'badge-success';
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
          <h1 className="page-title">Gestão de Turmas</h1>
          <p className="page-subtitle">Organize e gerencie as turmas escolares</p>
        </div>
        {canManage() && (
          <button className="btn btn-primary" onClick={() => handleOpenModal()}>
            <Plus size={20} />
            Nova Turma
          </button>
        )}
      </div>

      <div className="search-container">
        <Search className="search-icon" size={20} />
        <input
          type="text"
          className="search-input"
          placeholder="Buscar por nome, ano ou período..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
      </div>

      {filteredTurmas.length === 0 ? (
        <div className="empty-state">
          <BookOpen className="empty-state-icon" size={80} />
          <h3>Nenhuma turma encontrada</h3>
          <p>Comece criando uma nova turma</p>
        </div>
      ) : (
        <div className="table-container">
          <table className="table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>Ano</th>
                <th>Período</th>
                <th>Alunos</th>
                <th>Professores</th>
                {canManage() && <th>Ações</th>}
              </tr>
            </thead>
            <tbody>
              {filteredTurmas.map(turma => (
                <tr key={turma.id}>
                  <td><strong>{turma.nome}</strong></td>
                  <td>{turma.ano || '-'}</td>
                  <td>
                    <span className="badge badge-primary">
                      <Clock size={14} /> {turma.periodo || '-'}
                    </span>
                  </td>
                  <td>
                    <span className={`badge ${getCapacidadeBadge(turma)}`}>
                      <Users size={14} /> {turma.alunosCount || 0}{turma.capacidadeMaxima ? `/${turma.capacidadeMaxima}` : ''}
                    </span>
                  </td>
                  <td>
                    {turma.professores && turma.professores.length > 0
                      ? turma.professores.join(', ')
                      : '-'}
                  </td>
                  {canManage() && (
                    <td>
                      <div className="action-buttons">
                        <button
                          className="btn btn-icon btn-secondary"
                          onClick={() => handleOpenModal(turma)}
                          title="Editar"
                        >
                          <Edit2 size={16} />
                        </button>
                        <button
                          className="btn btn-icon btn-danger"
                          onClick={() => handleDelete(turma.id)}
                          title="Excluir"
                        >
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
              <h2>{editingTurma ? 'Editar Turma' : 'Nova Turma'}</h2>
              <button className="modal-close" onClick={handleCloseModal}>
                <X size={24} />
              </button>
            </div>
            <form onSubmit={handleSubmit}>
              <div className="modal-body">
                <div className="form-group">
                  <label className="form-label"><BookOpen size={16} /> Nome da Turma</label>
                  <input type="text" name="nome" className="form-input" value={formData.nome} onChange={handleInputChange} required placeholder="Ex: 1º Ano A" />
                </div>
                <div className="form-group">
                  <label className="form-label">Ano Letivo</label>
                  <input type="text" name="ano" className="form-input" value={formData.ano} onChange={handleInputChange} required placeholder="2024" />
                </div>
                <div className="form-group">
                  <label className="form-label"><Clock size={16} /> Período</label>
                  <select name="periodo" className="form-select" value={formData.periodo} onChange={handleInputChange} required>
                    <option value="Manhã">Manhã</option>
                    <option value="Tarde">Tarde</option>
                    <option value="Noite">Noite</option>
                    <option value="Integral">Integral</option>
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label"><Users size={16} /> Capacidade Máxima</label>
                  <input type="number" name="capacidadeMaxima" className="form-input" value={formData.capacidadeMaxima} onChange={handleInputChange} min="1" placeholder="30" />
                </div>
                {professores.length > 0 && (
                  <div className="form-group">
                    <label className="form-label">Professores (Ctrl+clique para selecionar vários)</label>
                    <select multiple name="professoresIds" className="form-select" style={{ height: '120px' }} onChange={handleProfessoresChange}>
                      {professores.map(p => (
                        <option key={p.id} value={p.id}>{p.nome} {p.sobrenome} - {p.disciplina}</option>
                      ))}
                    </select>
                  </div>
                )}
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={handleCloseModal}>
                  <X size={16} /> Cancelar
                </button>
                <button type="submit" className="btn btn-primary">
                  <Save size={16} /> {editingTurma ? 'Atualizar' : 'Cadastrar'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default TurmasPage;
