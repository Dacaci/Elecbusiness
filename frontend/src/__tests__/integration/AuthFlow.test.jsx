import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import App from '../../App';
import authService from '../../services/authService';

// Mock du service d'authentification
jest.mock('../../services/authService');

describe('Flux d\'authentification', () => {
    beforeEach(() => {
        // Réinitialiser localStorage et les mocks avant chaque test
        localStorage.clear();
        jest.clearAllMocks();
    });

    it('devrait afficher les boutons de connexion/inscription quand l\'utilisateur n\'est pas connecté', () => {
        authService.getCurrentUser.mockReturnValue(null);

        render(
            <BrowserRouter>
                <App />
            </BrowserRouter>
        );

        expect(screen.getByText('Connexion')).toBeInTheDocument();
        expect(screen.getByText('Inscription')).toBeInTheDocument();
    });

    it('devrait afficher le formulaire de recharge quand l\'utilisateur est connecté', () => {
        authService.getCurrentUser.mockReturnValue({ email: 'test@example.com' });

        render(
            <BrowserRouter>
                <App />
            </BrowserRouter>
        );

        expect(screen.getByText('Connecté en tant que : test@example.com')).toBeInTheDocument();
        expect(screen.getByLabelText('Numéro de téléphone:')).toBeInTheDocument();
        expect(screen.getByLabelText('Montant (FCFA):')).toBeInTheDocument();
    });

    it('devrait permettre la déconnexion', async () => {
        authService.getCurrentUser.mockReturnValue({ email: 'test@example.com' });

        render(
            <BrowserRouter>
                <App />
            </BrowserRouter>
        );

        const logoutButton = screen.getByText('Déconnexion');
        fireEvent.click(logoutButton);

        await waitFor(() => {
            expect(screen.getByText('Connexion')).toBeInTheDocument();
            expect(screen.getByText('Inscription')).toBeInTheDocument();
        });
    });

    it('devrait effectuer une recharge avec succès', async () => {
        authService.getCurrentUser.mockReturnValue({ email: 'test@example.com' });
        authService.getAuthHeader.mockReturnValue({ Authorization: 'Bearer mock-token' });

        // Mock de la réponse de l'API
        global.fetch = jest.fn().mockResolvedValue({
            json: () => Promise.resolve({ message: 'Recharge effectuée avec succès' })
        });

        render(
            <BrowserRouter>
                <App />
            </BrowserRouter>
        );

        // Remplir le formulaire
        fireEvent.change(screen.getByLabelText('Numéro de téléphone:'), {
            target: { value: '0123456789' }
        });
        fireEvent.change(screen.getByLabelText('Montant (FCFA):'), {
            target: { value: '1000' }
        });

        // Soumettre le formulaire
        fireEvent.click(screen.getByText('Recharger'));

        // Vérifier le message de succès
        await waitFor(() => {
            expect(screen.getByText('Recharge effectuée avec succès')).toBeInTheDocument();
        });
    });
}); 