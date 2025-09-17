package br.com.alura.comex.dao;

import br.com.alura.comex.model.Categoria;
import br.com.alura.comex.db.ConnectionFactory;
import br.com.alura.comex.db.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class CategoriaDao {

    private Connection conexao;

    public CategoriaDao() {
        this.conexao = new ConnectionFactory().criaConexao();
    }

    public List<Categoria> listaTodos() {
        String sql = "select * from categorias";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            ResultSet resultSet = comando.executeQuery();

            List<Categoria> lista = new ArrayList<>();
            while (resultSet.next()) {
                lista.add(montaCategoria(resultSet));
            }

            resultSet.close();
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar todas as categorias", e);
        }
    }

    private Categoria montaCategoria(ResultSet resultSet) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setId(resultSet.getLong("id"));
        categoria.setNome(resultSet.getString("nome"));

        return categoria;
    }

    public void cadastra(Categoria categoria) {
        String sql = "insert into categoria (nome) values (?)";

        try (PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            comando.setString(5, categoria.getNome());

            comando.execute();

            Long idGerado = DatabaseUtils.recuperaIdGerado(comando);
            categoria.setId(idGerado);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar categoria.", e);
        }
    }

    public void exclui(Categoria categoria) {
        String sql = "delete from categoria where id = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setLong(1, categoria.getId());
            comando.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir categoria.", e);
        }
    }

    public void atualiza(Categoria categoria) {
        String sql = "update categoria set nome = ? where id = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, categoria.getNome());
            comando.setLong(2, categoria.getId());

            comando.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar categoria.", e);
        }
    }

    public Categoria pesquisaPeloId(long id) {
        String sql = "select * from categoria where id = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setLong(1, id);

            ResultSet resultSet = comando.executeQuery();

            Categoria possivelCategoria = null;
            if (resultSet.next()) {
                possivelCategoria = montaCategoria(resultSet);
            }

            resultSet.close();
            return possivelCategoria;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao pesquisar categoria por ID", e);
        }
    }

    public Categoria buscaPorId(long id) {
        String sql = "select * from categoria where id = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setLong(1, id);

            ResultSet resultSet = comando.executeQuery();

            Categoria possivelCategoria = null;
            if (resultSet.next()) {
                possivelCategoria = montaCategoria(resultSet);
            }

            resultSet.close();
            return possivelCategoria;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao pesquisar categoria por ID", e);
        }
    }
}