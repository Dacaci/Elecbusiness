import { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import './App.css';
import authService from './services/authService';
import ProtectedRoute from './components/ProtectedRoute';

function App() {
  const [montant, setMontant] = useState('');
  const [numero, setNumero] = useState('');
  const [message, setMessage] = useState('');
  const [user, setUser] = useState(null);

  useEffect(() => {
    const currentUser = authService.getCurrentUser();
    if (currentUser) {
      setUser(currentUser);
    }
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('/api/recharge', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          ...authService.getAuthHeader()
        },
        body: JSON.stringify({ montant, numero }),
      });
      const data = await response.json();
      setMessage(data.message);
    } catch (error) {
      setMessage('Une erreur est survenue');
    }
  };

  const handleLogout = () => {
    authService.logout();
    setUser(null);
  };

  return (
    <Router>
      <div className="container">
        <h1>Service de Recharge</h1>
        {user ? (
          <>
            <div className="user-info">
              <p>Connecté en tant que : {user.email}</p>
              <button onClick={handleLogout} className="logout-btn">Déconnexion</button>
            </div>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label htmlFor="numero">Numéro de téléphone:</label>
                <input
                  type="tel"
                  id="numero"
                  value={numero}
                  onChange={(e) => setNumero(e.target.value)}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="montant">Montant (FCFA):</label>
                <input
                  type="number"
                  id="montant"
                  value={montant}
                  onChange={(e) => setMontant(e.target.value)}
                  required
                />
              </div>
              <button type="submit">Recharger</button>
            </form>
            {message && <p className="message">{message}</p>}
          </>
        ) : (
          <div className="auth-links">
            <a href="/login" className="auth-btn">Connexion</a>
            <a href="/register" className="auth-btn">Inscription</a>
          </div>
        )}
      </div>
    </Router>
  );
}

export default App; 