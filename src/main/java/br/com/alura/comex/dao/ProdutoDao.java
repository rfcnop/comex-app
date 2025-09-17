package br.com.alura.comex.dao;

import br.com.alura.comex.model.Produto;
import br.com.alura.comex.db.ConnectionFactory;
import br.com.alura.comex.db.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class ProdutoDao {

    private Connection conexao;

    public ProdutoDao() {
        this.conexao = new ConnectionFactory().criaConexao();
    }

    public void cadastra(Produto produto) {
        String sql = "insert into produto (nome, descricao, preco) values (?, ?, ?)";

        try (PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            comando.setString(1, produto.getNome());
            comando.setString(2, produto.getDescricao());
            comando.setDouble(3, produto.getPreco());

            comando.execute();
            Long idGerado = DatabaseUtils.recuperaIdGerado(comando);
            produto.setId(idGerado);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar produto.", e);
        }
    }

    public void atualiza(Produto produto) {
        String sql = "update produto set nome = ?, descricao = ?, preco = ? where id = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, produto.getNome());
            comando.setString(2, produto.getDescricao());
            comando.setDouble(3, produto.getPreco());
            comando.setLong(4, produto.getId());

            comando.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar produto.", e);
        }
    }

    public void exclui(Produto produto) {
        String sql = "delete from produto where id = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            // Exclui as categorias associadas ao produto
            excluiCategoriasProduto(produto);

            comando.setLong(1, produto.getId());
            comando.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir produto.", e);
        }
    }

    public List<Produto> listaTodos() {
        String sql = """
                select produto.*, categoria.*
                  from produto
                  left join categoria_produto on produto.id = categoria_produto.produto_id
                  left join categoria on categoria_produto.categoria_id = categoria.id
                 order by produto.id""";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            ResultSet resultSet = comando.executeQuery();

            List<Produto> produtos = new ArrayList<>();
            Produto produto = null;

            while (resultSet.next()) {
                Long produtoId = resultSet.getLong("produtos.id");

                if (produto == null || !produto.getId().equals(produtoId)) {
                    produto = montaProduto(resultSet);
                    produtos.add(produto);
                }

            }

            comando.close();
            return produtos;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar todos os produtos.", e);
        }
    }

    private void excluiCategoriasProduto(Produto produto) {
        String sql = "delete from categoria_produto where produto_id = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setLong(1, produto.getId());
            comando.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir categorias do produto.", e);
        }
    }

    private Produto montaProduto(ResultSet resultSet) throws SQLException {
        Produto produto = new Produto();
        produto.setId(resultSet.getLong("produto.id"));
        produto.setNome(resultSet.getString("produto.nome"));
        produto.setDescricao(resultSet.getString("produto.descricao"));
        produto.setPreco(resultSet.getDouble("produto.preco"));

        return produto;
    }
}