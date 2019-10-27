/**********************************************************************************************
  * Autor: Vitor de Almeida Reis
  * Matricula: 201710793
  * Inicio: 26/10/2019
  * Ultima alteracao: 27/10/2019 
  * Nome: Simulador do Leitor/Escritor
  * Funcao: Simulacao resolvendo problemas de concorrencia do leitor/escritor
  *********************************************************************************************/
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import controller.Controller;


public class Principal extends Application {

  @Override
  public void start(Stage primaryStage) {
    try{
      Parent principalfxml = FXMLLoader.load(getClass().getResource("/view/tela_principal.fxml")); // carrega tela em FXMl
      Scene scenePrincipal = new Scene(principalfxml); // cria cena
      primaryStage.setResizable(false); // seta como nao pode altera o tamanho do stage
      primaryStage.setScene(scenePrincipal); // seta cena no stage
      primaryStage.show(); // mostra o stage (tela)
    } catch(IOException e){
      e.printStackTrace();
    }

  }// fim start


  public static void main(String[] args){
    launch(args);
  }//fim método main
}// fim classe
