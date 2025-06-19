import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import ProtectedRoute from '../ProtectedRoute';
import authService from '../../services/authService';

// Mock du service d'authentification
jest.mock('../../services/authService');

describe('ProtectedRoute', () => {
    beforeEach(() => {
        // Réinitialiser les mocks avant chaque test
        jest.clearAllMocks();
    });

    it('devrait rediriger vers /login si l\'utilisateur n\'est pas connecté', () => {
        // Simuler un utilisateur non connecté
        authService.getCurrentUser.mockReturnValue(null);

        render(
            <BrowserRouter>
                <ProtectedRoute>
                    <div>Contenu protégé</div>
                </ProtectedRoute>
            </BrowserRouter>
        );

        // Vérifier que la redirection a eu lieu
        expect(window.location.pathname).toBe('/login');
    });

    it('devrait afficher le contenu si l\'utilisateur est connecté', () => {
        // Simuler un utilisateur connecté
        authService.getCurrentUser.mockReturnValue({ email: 'test@example.com' });

        render(
            <BrowserRouter>
                <ProtectedRoute>
                    <div>Contenu protégé</div>
                </ProtectedRoute>
            </BrowserRouter>
        );

        // Vérifier que le contenu est affiché
        expect(screen.getByText('Contenu protégé')).toBeInTheDocument();
    });
}); 