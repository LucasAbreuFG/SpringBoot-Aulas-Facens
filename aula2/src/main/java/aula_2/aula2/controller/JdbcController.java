package aula_2.aula2.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aula_2.aula2.entities.categoryEntitie;

@RestController
@RequestMapping("/teste")
public class JdbcController {

    @GetMapping
    public ResponseEntity<List<categoryEntitie>> teste() {

        String msg;
        List<categoryEntitie> list = new ArrayList<>();

        try {
            Class.forName("org.h2.Driver");
            msg = "<h1>Driver carregado com sucesso</h1>";


            String url = "jdbc:h2:mem:testedb";
            String user = "sa";
            String pwd = "";

            Connection con = DriverManager.getConnection(url, user, pwd);
            msg = "<h1>Conexão estabelecida com sucesso</h1>";

            Statement stm = con.createStatement();
            msg += "<h1>Statment criado com sucesso</h1>";
            
            stm.execute("drop table IF EXISTS tb_category");
            msg += "<h1>Drop da tabela caso exista!</h1>";

            
            stm.execute(" create table tb_category ("+
                        " id int8 generated by default as identity,"+
                        " created_at TIMESTAMP WITHOUT TIME ZONE,"+
                        " name varchar(255),"+
                        " primary key (id)) ");
            msg += "<h1>Criação da tabela!!</h1>";

            stm.execute("INSERT INTO tb_category (name, created_At) VALUES ('Livros', NOW())");
            stm.execute("INSERT INTO tb_category (name, created_At) VALUES ('Eletrônicos', NOW())");
            stm.execute("INSERT INTO tb_category (name, created_At) VALUES ('Computadores', NOW());");
            msg += "<h1>Insert da tabela!!</h1>";
            
            ResultSet rs = stm.executeQuery("SELECT *FROM tb_category");
            msg += "<h1>Select Executado!!</h1>";
            
            while(rs.next()){
                categoryEntitie c = new categoryEntitie();
                    c.setId(rs.getLong("id"));
                    c.setName(rs.getString("name"));

                    list.add(c);
            }

            
        } catch (ClassNotFoundException e) {

            msg = "<h1>Erro ao carregar o Driver</h1>";
            e.printStackTrace();

        }
        catch(SQLException e){
            msg = "<h1>Erro ao se conectar ao banco</h1>";
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(list);
    }

}
