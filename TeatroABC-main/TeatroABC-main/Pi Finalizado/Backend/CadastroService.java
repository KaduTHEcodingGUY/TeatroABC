package Backend;

import Utilitarios.ValidadorCPF;
public class CadastroService {


    public void cadastrar(String nome, String email, String cpf, String telefone, String senha) {
        if (nome.isEmpty() || email.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || senha.isEmpty()) {
            throw new IllegalArgumentException("Preencha todos os campos!");
        }

        if (!ValidadorCPF.isCPF(cpf)) {
            throw new IllegalArgumentException("CPF inválido!");
        }

        
    }
}
