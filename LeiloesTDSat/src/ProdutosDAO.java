import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;  


public class ProdutosDAO {
    
    Connection conn;
    PreparedStatement prep;
    ResultSet resultset;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();
    
    public void cadastrarProduto (ProdutosDTO produto){
    String sql = "INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)";

    try (Connection conn = new conectaDAO().connectDB(); // Usando try-with-resources
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        // Definindo os parâmetros da consulta
        stmt.setString(1, produto.getNome());   // Nome do produto
        stmt.setInt(2, produto.getValor());     // Valor do produto
        stmt.setString(3, produto.getStatus()); // Status do produto
        
        // Executando a inserção no banco
        stmt.executeUpdate();
        
    } catch (SQLException e) {
        // Exibe mensagem de erro se algo der errado
        JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto: " + e.getMessage());
    }
        
    }
    
    
    public boolean venderProduto(int idProduto) {
      
     
        
        String sql = "UPDATE produtos SET status = ? WHERE id_produto = ?";

        try 
            // Estabelece a conexão com o banco de dados
            (Connection conn = new conectaDAO().connectDB();
            PreparedStatement stmt = conn.prepareStatement(sql)){
        
            stmt.setString(1, "Vendido"); // Define o status como "Vendido"
            stmt.setInt(2, idProduto); // Define o id do produto a ser atualizado

            int rowsAffected = stmt.executeUpdate();

            // Se a atualização afetou uma linha, a operação foi bem-sucedida
            if (rowsAffected > 0) {
                sucesso = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return sucesso;
    }

    // Método para listar todos os produtos com status "Vendido"
    public listagem<Produto> listarProdutosVendidos() {
        listagem<Produto> produtosVendidos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Estabelece a conexão com o banco de dados
            conn = ConnectionFactory.getConnection(); // Altere conforme sua classe de conexão
            String sql = "SELECT * FROM produtos WHERE status = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "Vendido");

            rs = stmt.executeQuery();

            // Adiciona os produtos vendidos à lista
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id_produto"));
                produto.setNome(rs.getString("nome"));
                produto.setStatus(rs.getString("status"));
                // Adicione outros campos do produto conforme necessário

                produtosVendidos.add(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return produtosVendidos;
    }

        
    
    public ArrayList<ProdutosDTO> listarProdutos() {
    String sql = "SELECT * FROM produtos"; // Consulta para buscar todos os produtos
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();

    try (Connection conn = new conectaDAO().connectDB();  // Usando try-with-resources
         PreparedStatement prep = conn.prepareStatement(sql);
         ResultSet resultset = prep.executeQuery()) {
        
        while (resultset.next()) {
            ProdutosDTO produto = new ProdutosDTO();
            produto.setId(resultset.getInt("id"));  // Mantendo Integer para id
            produto.setNome(resultset.getString("nome"));
            produto.setValor(resultset.getInt("valor"));  // Mantendo Integer para valor
            produto.setStatus(resultset.getString("status"));
            listagem.add(produto);  // Adiciona o produto à lista
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Erro ao listar produtos: " + e.getMessage());
    }

    return listagem;
}
       
}

