package br.com.alura.comex.dao;

import br.com.alura.comex.model.Cliente;
import br.com.alura.comex.db.ConnectionFactory;
import br.com.alura.comex.db.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao {

    private Connection conexao;

    public ClienteDao() {
        this.conexao = new ConnectionFactory().criaConexao();
    }

    public List<Cliente> listaTodos() {
        String sql = "select * from cliente";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            ResultSet resultSet = comando.executeQuery();

            List<Cliente> lista = new ArrayList<>();
            while (resultSet.next()) {
                lista.add(montaCliente(resultSet));
            }

            resultSet.close();

            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar todos os clientes", e);
        }
    }

    private Cliente montaCliente(ResultSet resultSet) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(resultSet.getLong("id"));
        cliente.setNome(resultSet.getString("nome"));
        cliente.setCpf(resultSet.getString("cpf"));
        cliente.setEmail(resultSet.getString("email"));
        cliente.setTelefone(resultSet.getString("telefone"));
        cliente.setLogradouro(resultSet.getString("logradouro"));
        cliente.setBairro(resultSet.getString("bairo"));
        cliente.setCidade(resultSet.getString("cidade"));
        cliente.setEstado(resultSet.getString("uf"));
        cliente.setCep(resultSet.getString("cep"));

        return cliente;
    }

    public void cadastra(Cliente cliente) {
        String sql = """
                     insert into cliente 
                        (nome, email, tel, cpf, logradouro, bairro, cidade, uf, cep) 
                     values
                        (?, ?, ?, ?, ?, ?, ?, ?, ?)
                     """;

        try (PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            comando.setString(1, cliente.getNome());
            comando.setString(2, cliente.getEmail());
            comando.setString(3, cliente.getTelefone());
            comando.setString(4, cliente.getCpf());
            comando.setString(5, cliente.getLogradouro());
            comando.setString(6, cliente.getBairro());
            comando.setString(7, cliente.getCidade());
            comando.setString(8, cliente.getEstado());
            comando.setString(9, cliente.getCep());

            cliente.setId(DatabaseUtils.recuperaIdGerado(comando));

            comando.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Deu merda na hora de salvar...", e);
        }
    }

    public void exclui(Cliente cliente) {
        String sql = "delete from cliente where id = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setLong(1, cliente.getId());

            comando.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir cliente.", e);
        }
    }

    public void atualiza(Cliente cliente) {
        String sql = """
                     update cliente set 
                        nome = ?, 
                        email = ?, 
                        telefone = ?, 
                        cpf = ?, 
                        logradouro = ?, 
                        bairro = ?, 
                        cidade = ?, 
                        uf = ?, 
                        cep = ? 
                     where id = ?
                     """;

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, cliente.getNome());
            comando.setString(2, cliente.getEmail());
            comando.setString(3, cliente.getTelefone());
            comando.setString(4, cliente.getCpf());
            comando.setString(5, cliente.getLogradouro());
            comando.setString(6, cliente.getBairro());
            comando.setString(7, cliente.getCidade());
            comando.setString(8, cliente.getEstado());
            comando.setString(9, cliente.getCep());
            comando.setLong(10, cliente.getId());

            comando.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente.", e);
        }
    }

    public Cliente pesquisaPeloId(long id) {
        String sql = "select * from cliente where id = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setLong(1, id);

            ResultSet resultSet = comando.executeQuery();

            Cliente possivelCliente = null;
            if (resultSet.next()) {
                possivelCliente = montaCliente(resultSet);
            }

            resultSet.close();
            return possivelCliente;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao pesquisar cliente por ID", e);
        }
    }
}
