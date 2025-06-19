import { useState } from 'react'
import './App.css'

function App() {
  const [montant, setMontant] = useState('')
  const [numero, setNumero] = useState('')
  const [message, setMessage] = useState('')

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      const response = await fetch('/api/recharge', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ montant, numero }),
      })
      const data = await response.json()
      setMessage(data.message)
    } catch (error) {
      setMessage('Une erreur est survenue')
    }
  }

  return (
    <div className="container">
      <h1>Service de Recharge</h1>
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
    </div>
  )
}

export default App 