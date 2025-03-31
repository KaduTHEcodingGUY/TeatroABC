package Backend;

import Utilitarios.ValidadorCPF;
public class CadastroService {
    private final ValidadorCPF validadorCPF;

    public CadastroService(ValidadorCPF validadorCPF) {
        this.validadorCPF = validadorCPF;
    }

    public void cadastrar(String nome, String email, String cpf, String telefone, String senha) {
        if (nome.isEmpty() || email.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || senha.isEmpty()) {
            throw new IllegalArgumentException("Preencha todos os campos!");
        }

        if (!ValidadorCPF.isCPF(cpf)) {
            throw new IllegalArgumentException("CPF inválido!");
        }

        // Aqui no futuro será onde o usuário será salvo no banco
        System.out.println("Usuário cadastrado: " + nome);
    }
}
