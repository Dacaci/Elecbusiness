import authService from '../authService';
import axios from 'axios';

// Mock d'axios
jest.mock('axios');

describe('authService', () => {
    beforeEach(() => {
        // Réinitialiser localStorage avant chaque test
        localStorage.clear();
        // Réinitialiser les mocks
        jest.clearAllMocks();
    });

    describe('login', () => {
        it('devrait stocker le token dans localStorage après une connexion réussie', async () => {
            const mockToken = 'mock-jwt-token';
            const mockResponse = { data: { token: mockToken } };
            axios.post.mockResolvedValue(mockResponse);

            await authService.login('test@example.com', 'password');

            expect(localStorage.getItem('user')).toBe(JSON.stringify({ token: mockToken }));
        });

        it('devrait gérer les erreurs de connexion', async () => {
            axios.post.mockRejectedValue(new Error('Erreur de connexion'));

            await expect(authService.login('test@example.com', 'wrong-password'))
                .rejects.toThrow('Erreur de connexion');
        });
    });

    describe('register', () => {
        it('devrait stocker le token dans localStorage après une inscription réussie', async () => {
            const mockToken = 'mock-jwt-token';
            const mockResponse = { data: { token: mockToken } };
            axios.post.mockResolvedValue(mockResponse);

            const userData = {
                email: 'test@example.com',
                password: 'password',
                pseudo: 'testuser',
                nom: 'Test',
                prenom: 'User'
            };

            await authService.register(userData);

            expect(localStorage.getItem('user')).toBe(JSON.stringify({ token: mockToken }));
        });
    });

    describe('logout', () => {
        it('devrait supprimer les données utilisateur de localStorage', () => {
            localStorage.setItem('user', JSON.stringify({ token: 'mock-token' }));
            
            authService.logout();

            expect(localStorage.getItem('user')).toBeNull();
        });
    });

    describe('getCurrentUser', () => {
        it('devrait retourner les données utilisateur si présentes dans localStorage', () => {
            const mockUser = { token: 'mock-token', email: 'test@example.com' };
            localStorage.setItem('user', JSON.stringify(mockUser));

            const currentUser = authService.getCurrentUser();

            expect(currentUser).toEqual(mockUser);
        });

        it('devrait retourner null si aucune donnée utilisateur n\'est présente', () => {
            const currentUser = authService.getCurrentUser();

            expect(currentUser).toBeNull();
        });
    });

    describe('getAuthHeader', () => {
        it('devrait retourner l\'en-tête d\'autorisation si un utilisateur est connecté', () => {
            const mockUser = { token: 'mock-token' };
            localStorage.setItem('user', JSON.stringify(mockUser));

            const authHeader = authService.getAuthHeader();

            expect(authHeader).toEqual({ Authorization: 'Bearer mock-token' });
        });

        it('devrait retourner un objet vide si aucun utilisateur n\'est connecté', () => {
            const authHeader = authService.getAuthHeader();

            expect(authHeader).toEqual({});
        });
    });
}); 