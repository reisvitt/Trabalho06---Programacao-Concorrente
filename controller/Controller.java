package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.fxml.Initializable;
import java.util.ResourceBundle;
import java.net.URL;
import javafx.application.Application;
import javafx.application.Platform;
import model.Escritor;
import model.Leitor;
import java.util.concurrent.Semaphore;
import javafx.concurrent.Task;

/******************************************************************
 * Classe: Controller
 * Funcao: Classe reponsavel por controlar todas as
 *         acoes da view tela_principal.fxml
 *****************************************************************/
public class Controller implements Initializable{
  @FXML
  private ImageView imageViewQuadro;

  @FXML
  private Slider sliderEP1, sliderEP2, sliderEP3, sliderEP4, sliderEP5, sliderEE1, sliderEE2, sliderEE3, sliderEE4, sliderEE5; 

  @FXML
  private Slider sliderLP1, sliderLP2, sliderLP3, sliderLP4, sliderLP5, sliderLE1, sliderLE2, sliderLE3, sliderLE4, sliderLE5;

  @FXML
  private Line lineE1, lineE2, lineE3, lineE4, lineE5;

  @FXML
  private Line lineL1, lineL2, lineL3, lineL4, lineL5;

  @FXML
  private AnchorPane escritor1, escritor2, escritor3, escritor4, escritor5;

  @FXML
  private AnchorPane leitor1, leitor2, leitor3, leitor4, leitor5;

  @FXML
  private Button buttonDownEst, buttonUpEst, buttonDownProf, buttonUpProf;

  @FXML
  AnchorPane anchorPaneHelp;
  private static final int QTD = 5; 

  //controle da parte grafica
  private boolean profVisiveis[] = new boolean[QTD];
  private boolean estVisiveis[] = new boolean[QTD];
  private int qtdProfVisiveis = 1;
  private int qtdEstVisiveis = 1;

  // Escritores e Leitores
  private Escritor professor[] = new Escritor[QTD];
  private Leitor estudante[] = new Leitor[QTD];

  //Semaforos e variavel de controle
  private Semaphore mutex = new Semaphore(1);
  private Semaphore mutexLeitor = new Semaphore(1);
  private int quantidadeLeitor = 0;


  public Controller(){ 
    professor[0] = new Escritor(0, this);
    estudante[0] = new Leitor(0, this);
    
  }

  /*************************************************************************
   * Metodo: initialize
   * Funcao: primeiro metodo inicializado do javafx
   * Parametros: URL, ResourceBundle
   * Retorno: void
   ************************************************************************/
  public void initialize(URL url, ResourceBundle rb){
    configuracoesPadrao();
  }

  /*************************************************************************
   * Metodo: configuracoesPadrao
   * Funcao: configuracoes padrao da scene
   * Parametros: void
   * Retorno: void
   ************************************************************************/
  public void configuracoesPadrao(){

    // Buttons Down/Up
    buttonDownEst.setDisable(true);
    buttonDownProf.setDisable(true);

    // Help
    anchorPaneHelp.setVisible(false);

    // Escritores/professores off
    escritor2.setVisible(false);
    lineE2.setVisible(false);
    escritor3.setVisible(false);
    lineE3.setVisible(false);
    escritor4.setVisible(false);
    lineE4.setVisible(false);
    escritor5.setVisible(false);
    lineE5.setVisible(false);

    // Leitores/estudantes off
    leitor2.setVisible(false);
    lineL2.setVisible(false);
    leitor3.setVisible(false);
    lineL3.setVisible(false);
    leitor4.setVisible(false);
    lineL4.setVisible(false);
    leitor5.setVisible(false);
    lineL5.setVisible(false);

    // Iniciar Threads
    professor[0].start();
    estudante[0].start();

  }

  /************************************************************************
   * Metodo: downMutex
   * Funcao: down no mutex reponsavel por proteger o recurso compartilhado
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  public void downMutex() throws Exception {
    mutex.acquire(); // entra RC
  }

  /************************************************************************
   * Metodo: acessarRCEscritor
   * Funcao: escritor acessa o recurso compartilhado
   * Parametros: int -> ID
   * Retorno: void
   ***********************************************************************/
  public void acessarRCEscritor(int id) throws Exception {
    Platform.runLater(() -> {
      switch(id){
      case 0:
        imageViewQuadro.setImage(new Image("images/equation1.jpg"));
        break;
      case 1:
        imageViewQuadro.setImage(new Image("images/equation2.jpg"));
        break;
      case 2:
        imageViewQuadro.setImage(new Image("images/equation3.jpg"));
        break;
      case 3:
        imageViewQuadro.setImage(new Image("images/equation4.jpg"));
        break;
      case 4:
        imageViewQuadro.setImage(new Image("images/equation5.jpg"));
        break;
    }
    });
    
  }

  /************************************************************************
   * Metodo: upMutex
   * Funcao: libera o recurso compartilhado.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  public void upMutex(){
    mutex.release();
  }

  /************************************************************************
   * Metodo: acessarRCLeitor
   * Funcao: caso seja o primeiro leitor a acessar, nao permite a entrada 
   *         de escritores ao recuso compartilhado.
   * Parametros: int -> ID
   * Retorno: void
   ***********************************************************************/
  public void acessarRCLeitor(int id) throws Exception {
    
    mutexLeitor.acquire();
    if(quantidadeLeitor == 0){
      downMutex();
    }
    quantidadeLeitor++;
    mutexLeitor.release();
  }

  /************************************************************************
   * Metodo: deixarRCLeitor
   * Funcao: caso seja o ultimo leitor a sair, permite a entrada 
   *         de escritores ao recuso compartilhado.
   * Parametros: int -> ID
   * Retorno: void
   ***********************************************************************/
  public void deixarRCLeitor(int id) throws Exception {
    animacaoForaRCLeitor(id);
    mutexLeitor.acquire();
    if(quantidadeLeitor == 1){
      quantidadeLeitor--;
      upMutex();
    }else{
      quantidadeLeitor--;
    }
    mutexLeitor.release();
  }

  /************************************************************************
   * Metodo: animacaoIntencaoEntrarRCEscritor
   * Funcao: muda a cor da animacao para amarelo
   * Parametros: int -> ID
   * Retorno: void
   ***********************************************************************/
  public void animacaoIntencaoEntrarRCEscritor(int id){
    Platform.runLater(() -> {
      switch(id){
      case 0:
        escritor1.setStyle(cores("amarelo"));
        lineE1.setStyle("-fx-Stroke: yellow;");
        break;
      case 1:
        escritor2.setStyle(cores("amarelo"));
        lineE2.setStyle("-fx-Stroke: yellow;");
        break;
      case 2:
        escritor3.setStyle(cores("amarelo"));
        lineE3.setStyle("-fx-Stroke: yellow;");
        break;
      case 3:
        escritor4.setStyle(cores("amarelo"));
        lineE4.setStyle("-fx-Stroke: yellow;");
        break;
      case 4:
        escritor5.setStyle(cores("amarelo"));
        lineE5.setStyle("-fx-Stroke: yellow;");
        break;
    }
    });
    
  }

  /************************************************************************
   * Metodo: animacaoIntencaoEntrarRCLeitor
   * Funcao: muda a cor da animacao para amarelo
   * Parametros: int -> ID
   * Retorno: void
   ***********************************************************************/
  public void animacaoIntencaoEntrarRCLeitor(int id){
    Platform.runLater(() -> {
      switch(id){
      case 0:
        leitor1.setStyle(cores("amarelo"));
        lineL1.setStyle("-fx-Stroke: yellow;");
        break;
      case 1:
        leitor2.setStyle(cores("amarelo"));
        lineL2.setStyle("-fx-Stroke: yellow;");
        break;
      case 2:
        leitor3.setStyle(cores("amarelo"));
        lineL3.setStyle("-fx-Stroke: yellow;");
        break;
      case 3:
        leitor4.setStyle(cores("amarelo"));
        lineL4.setStyle("-fx-Stroke: yellow;");
        break;
      case 4:
        leitor5.setStyle(cores("amarelo"));
        lineL5.setStyle("-fx-Stroke: yellow;");
        break;
    }
    });
    
  }

  /************************************************************************
   * Metodo: animacaoDentroRCEscritor
   * Funcao: muda a cor da animacao para vermelho
   * Parametros: int -> ID
   * Retorno: void
   ***********************************************************************/
  public void animacaoDentroRCEscritor(int id){
    Platform.runLater(() -> {
      switch(id){
      case 0:
        escritor1.setStyle(cores("vermelho"));
        lineE1.setStyle("-fx-Stroke: red;");
        break;
      case 1:
        escritor2.setStyle(cores("vermelho"));
        lineE2.setStyle("-fx-Stroke: red;");
        break;
      case 2:
        escritor3.setStyle(cores("vermelho"));
        lineE3.setStyle("-fx-Stroke: red;");
        break;
      case 3:
        escritor4.setStyle(cores("vermelho"));
        lineE4.setStyle("-fx-Stroke: red;");
        break;
      case 4:
        escritor5.setStyle(cores("vermelho"));
        lineE5.setStyle("-fx-Stroke: red;");
        break;
    }
    });
    
  }

  /************************************************************************
   * Metodo: animacaoDentroRCLeitor
   * Funcao: muda a cor da animacao para vermelho
   * Parametros: int -> ID
   * Retorno: void
   ***********************************************************************/
  public void animacaoDentroRCLeitor(int id){
    Platform.runLater(() -> {
      switch(id){
      case 0:
        leitor1.setStyle(cores("vermelho"));
        lineL1.setStyle("-fx-Stroke: red;");
        break;
      case 1:
        leitor2.setStyle(cores("vermelho"));
        lineL2.setStyle("-fx-Stroke: red;");
        break;
      case 2:
        leitor3.setStyle(cores("vermelho"));
        lineL3.setStyle("-fx-Stroke: red;");
        break;
      case 3:
        leitor4.setStyle(cores("vermelho"));
        lineL4.setStyle("-fx-Stroke: red;");
        break;
      case 4:
        leitor5.setStyle(cores("vermelho"));
        lineL5.setStyle("-fx-Stroke: red;");
        break;
    }
    });
    
  }

  /************************************************************************
   * Metodo: animacaoForaRCEscritor
   * Funcao: muda a cor da animacao para verde
   * Parametros: int -> ID
   * Retorno: void
   ***********************************************************************/
  public void animacaoForaRCEscritor(int id){
    Platform.runLater(() -> {
      switch(id){
      case 0:
        escritor1.setStyle(cores("verde"));
        lineE1.setStyle("-fx-Stroke: green;");
        break;
      case 1:
        escritor2.setStyle(cores("verde"));
        lineE2.setStyle("-fx-Stroke: green;");
        break;
      case 2:
        escritor3.setStyle(cores("verde"));
        lineE3.setStyle("-fx-Stroke: green;");
        break;
      case 3:
        escritor4.setStyle(cores("verde"));
        lineE4.setStyle("-fx-Stroke: green;");
        break;
      case 4:
        escritor5.setStyle(cores("verde"));
        lineE5.setStyle("-fx-Stroke: green;");
        break;
    }
    });
    
  }

  /************************************************************************
   * Metodo: animacaoForaRCLeitor
   * Funcao: muda a cor da animacao para verde
   * Parametros: int -> ID
   * Retorno: void
   ***********************************************************************/
  public void animacaoForaRCLeitor(int id){
    Platform.runLater(() -> {
      switch(id){
      case 0:
        leitor1.setStyle(cores("verde"));
        lineL1.setStyle("-fx-Stroke: green;");
        break;
      case 1:
        leitor2.setStyle(cores("verde"));
        lineL2.setStyle("-fx-Stroke: green;");
        break;
      case 2:
        leitor3.setStyle(cores("verde"));
        lineL3.setStyle("-fx-Stroke: green;");
        break;
      case 3:
        leitor4.setStyle(cores("verde"));
        lineL4.setStyle("-fx-Stroke: green;");
        break;
      case 4:
        leitor5.setStyle(cores("verde"));
        lineL5.setStyle("-fx-Stroke: green;");
        break;
    }
    });
    
  }

  /************************************************************************
   * Metodo: showHelp
   * Funcao: mostra o pane de Help, com todos as informacoes necessarias
   *         para compreender a simulacao.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void showHelp(){
    anchorPaneHelp.setVisible(true);
  }

  /************************************************************************
   * Metodo: closeHelp
   * Funcao: oculta o pane de Help.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void closeHelp(){
    anchorPaneHelp.setVisible(false);
  }

  /************************************************************************
   * Metodo: profUp
   * Funcao: aumenta a quantidade de professores (escritores).
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void profUp(){
    switch(qtdProfVisiveis){
      case 1:
        professor[1] = new Escritor(1, this);
        professor[1].start();

        lineE2.setVisible(true);
        escritor2.setVisible(true);
        qtdProfVisiveis++;
        buttonDownProf.setDisable(false);
        break;
      case 2:
        professor[2] = new Escritor(2, this);
        professor[2].start();
        
        lineE3.setVisible(true);
        escritor3.setVisible(true);
        qtdProfVisiveis++;
        break;
      case 3:
        professor[3] = new Escritor(3, this);
        professor[3].start();
        
        lineE4.setVisible(true);
        escritor4.setVisible(true);
        qtdProfVisiveis++;
        break;
      case 4:
        professor[4] = new Escritor(4, this);
        professor[4].start();
        
        lineE5.setVisible(true);
        escritor5.setVisible(true);
        qtdProfVisiveis++;
        buttonUpProf.setDisable(true);
        break;
    }
  }

  /************************************************************************
   * Metodo: profDown
   * Funcao: diminui a quantidade de professores (escritores).
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void profDown(){
    switch(qtdProfVisiveis){
      case 2:
        professor[1].stop();
        if(professor[1].getAcessandoRC()){
          upMutex();
          professor[1].setAcessandoRC(false);
        }
        animacaoForaRCEscritor(1);
        lineE2.setVisible(false);
        escritor2.setVisible(false);
        qtdProfVisiveis--;
        buttonDownProf.setDisable(true);
        break;
      case 3:
        professor[2].stop();
        if(professor[2].getAcessandoRC()){
          upMutex();
          professor[2].setAcessandoRC(false);
        }
        
        animacaoForaRCEscritor(2);
        lineE3.setVisible(false);
        escritor3.setVisible(false);
        qtdProfVisiveis--;
        break;
      case 4:
        professor[3].stop();
        if(professor[3].getAcessandoRC()){
          upMutex();
          professor[3].setAcessandoRC(false);
        }

        animacaoForaRCEscritor(3);
        lineE4.setVisible(false);
        escritor4.setVisible(false);
        qtdProfVisiveis--;
        break;
      case 5:
        professor[4].stop();
        if(professor[4].getAcessandoRC()){
          upMutex();
          professor[4].setAcessandoRC(false);
        }

        animacaoForaRCEscritor(4);
        lineE5.setVisible(false);
        escritor5.setVisible(false);
        qtdProfVisiveis--;
        buttonUpProf.setDisable(false);
        break;
    }
  }

  /************************************************************************
   * Metodo: estUp
   * Funcao: aumenta a quantidade de estudantes (leitores).
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void estUp(){
    switch(qtdEstVisiveis){
      case 1:
        
        estudante[1] = null;
        estudante[1] = new Leitor(1, this);
        estudante[1].start();

        lineL2.setVisible(true);
        leitor2.setVisible(true);
        qtdEstVisiveis++;

        buttonDownEst.setDisable(false);
        break;
      case 2:
        estudante[2] = null;
        estudante[2] = new Leitor(2, this);
        estudante[2].start();
        
        lineL3.setVisible(true);
        leitor3.setVisible(true);

        qtdEstVisiveis++;
        break;
      case 3:
        estudante[3] = null;
        estudante[3] = new Leitor(3, this);
        estudante[3].start();
        
        lineL4.setVisible(true);
        leitor4.setVisible(true);

        qtdEstVisiveis++;
        break;
      case 4:
        estudante[4] = null;
        estudante[4] = new Leitor(4, this);
        estudante[4].start();
        
        lineL5.setVisible(true);
        leitor5.setVisible(true);
        qtdEstVisiveis++;
        buttonUpEst.setDisable(true);
        break;
    }
  }

  /************************************************************************
   * Metodo: estDown
   * Funcao: diminui a quantidade de estudantes (leitores).
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void estDown() throws Exception {
    switch(qtdEstVisiveis){
      case 2:
        estudante[1].stop();
        
        mutexLeitor.acquire();
        if(estudante[1].getAcessandoRC() && quantidadeLeitor == 1){
          quantidadeLeitor--;
          upMutex();
        }else if(estudante[1].getAcessandoRC()){
          quantidadeLeitor--;
        }

        estudante[1].setAcessandoRC(false);
        mutexLeitor.release();

        animacaoForaRCLeitor(1);
        lineL2.setVisible(false);
        leitor2.setVisible(false);
        qtdEstVisiveis--;
        buttonDownEst.setDisable(true);
        break;
      case 3:
        estudante[2].stop();
        
        mutexLeitor.acquire();
        if(estudante[2].getAcessandoRC() && quantidadeLeitor == 1){
          quantidadeLeitor--;
          upMutex();
        }else if(estudante[2].getAcessandoRC()){
          quantidadeLeitor--;
        }

        estudante[2].setAcessandoRC(false);
        mutexLeitor.release();
        
        animacaoForaRCLeitor(2);
        lineL3.setVisible(false);
        leitor3.setVisible(false);
        qtdEstVisiveis--;
        break;
      case 4:
        estudante[3].stop();
        
        mutexLeitor.acquire();
        if(estudante[3].getAcessandoRC() && quantidadeLeitor == 1){
          quantidadeLeitor--;
          upMutex();
        }else if(estudante[3].getAcessandoRC()){
          quantidadeLeitor--;
        }

        estudante[3].setAcessandoRC(false);
        mutexLeitor.release();

        animacaoForaRCLeitor(3);
        lineL4.setVisible(false);
        leitor4.setVisible(false);
        qtdEstVisiveis--;
        break;
      case 5:
        estudante[4].stop();

        mutexLeitor.acquire();
        if(estudante[4].getAcessandoRC() && quantidadeLeitor == 1){
          quantidadeLeitor--;
          upMutex();
        }else if(estudante[4].getAcessandoRC()){
          quantidadeLeitor--;
        }

        estudante[4].setAcessandoRC(false);
        mutexLeitor.release();
        
        animacaoForaRCLeitor(4);
        lineL5.setVisible(false);
        leitor5.setVisible(false);
        qtdEstVisiveis--;
        buttonUpEst.setDisable(false);
        break;
    }
  }

  /************************************************************************
   * Metodo: cores
   * Funcao: retorna os estilos de cor para cada animacao
   * Parametros: String -> ID
   * Retorno: String -> estilos
   ***********************************************************************/
  public String cores(String id){
    String valor = "";
    switch(id){
      case "amarelo":
        valor = ("-fx-pref-width: 215; -fx-pref-height: 135; -fx-border-radius: 5px; -fx-border-width: 0.5; -fx-border-color: yellow;");
        break;
      case "verde":
        valor = ("-fx-pref-width: 215; -fx-pref-height: 135; -fx-border-radius: 5px; -fx-border-width: 0.5; -fx-border-color: green;");
        break;
      case "vermelho":
        valor = ("-fx-pref-width: 215; -fx-pref-height: 135; -fx-border-radius: 5px; -fx-border-width: 0.5; -fx-border-color: red;");
        break;
    }
    return valor;
  }

  /************************************************************************
   * Metodo: sliderEP1
   * Funcao: controla a velocidade de produzao do professor1.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void sliderEP1(){
    int valor = (int) sliderEP1.getValue();

    professor[0].setVelProduzir(valor);
  }

  /************************************************************************
   * Metodo: sliderEP2
   * Funcao: controla a velocidade de produzao do professor2.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void sliderEP2(){
    int valor = (int) sliderEP2.getValue();

    professor[1].setVelProduzir(valor);
  }

  /************************************************************************
   * Metodo: sliderEP3
   * Funcao: controla a velocidade de produzao do professor3.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void sliderEP3(){
    int valor = (int) sliderEP3.getValue();

    professor[2].setVelProduzir(valor);
  }

  /************************************************************************
   * Metodo: sliderEP4
   * Funcao: controla a velocidade de produzao do professor4.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void sliderEP4(){
    int valor = (int) sliderEP4.getValue();

    professor[3].setVelProduzir(valor);
  }

  /************************************************************************
   * Metodo: sliderEP5
   * Funcao: controla a velocidade de produzao do professor5.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void sliderEP5(){
    int valor = (int) sliderEP5.getValue();

    professor[4].setVelProduzir(valor);
  }

  /************************************************************************
   * Metodo: sliderEE1
   * Funcao: controla a velocidade de exibicao do professor1.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void sliderEE1(){
    int valor = (int) sliderEE1.getValue();

    professor[0].setVelEscrever(valor);
  }

  /************************************************************************
   * Metodo: sliderEE2
   * Funcao: controla a velocidade de exibicao do professor3.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void sliderEE2(){
    int valor = (int) sliderEE2.getValue();

    professor[1].setVelEscrever(valor);
  }

  /************************************************************************
   * Metodo: sliderEE3
   * Funcao: controla a velocidade de exibicao do professor3.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void sliderEE3(){
    int valor = (int) sliderEE3.getValue();

    professor[2].setVelEscrever(valor);
  }

  /************************************************************************
   * Metodo: sliderEE4
   * Funcao: controla a velocidade de exibicao do professor4.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void sliderEE4(){
    int valor = (int) sliderEE4.getValue();

    professor[3].setVelEscrever(valor);
  }

  /************************************************************************
   * Metodo: sliderEE5
   * Funcao: controla a velocidade de exibicao do professor5.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void sliderEE5(){
    int valor = (int) sliderEE5.getValue();

    professor[4].setVelEscrever(valor);
  }

  /************************************************************************
   * Metodo: sliderLP1
   * Funcao: controla a velocidade de vizualizacao do estudante1.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void sliderLP1(){
    int valor = (int) sliderLP1.getValue();

    estudante[0].setVelVizualizar(valor);
  }
  /************************************************************************
   * Metodo: sliderLP2
   * Funcao: controla a velocidade de vizualizacao do estudante2.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void sliderLP2(){
    int valor = (int) sliderLP2.getValue();

    estudante[1].setVelVizualizar(valor);
  }

  /************************************************************************
   * Metodo: sliderLP3
   * Funcao: controla a velocidade de vizualizacao do estudante3.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void sliderLP3(){
    int valor = (int) sliderLP3.getValue();

    estudante[2].setVelVizualizar(valor);
  }

  /************************************************************************
   * Metodo: sliderLP4
   * Funcao: controla a velocidade de vizualizacao do estudante4.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void sliderLP4(){
    int valor = (int) sliderLP4.getValue();

    estudante[3].setVelVizualizar(valor);
  }

  /************************************************************************
   * Metodo: sliderLP5
   * Funcao: controla a velocidade de vizualizacao do estudante5.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void sliderLP5(){
    int valor = (int) sliderLP5.getValue();

    estudante[4].setVelVizualizar(valor);
  }

  /************************************************************************
   * Metodo: sliderLE1
   * Funcao: controla a velocidade de absorcao do estudante1.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void sliderLE1(){
    int valor = (int) sliderLE1.getValue();

    estudante[0].setVelAbsorver(valor);
  }

  /************************************************************************
   * Metodo: sliderLE2
   * Funcao: controla a velocidade de absorcao do estudante2.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void sliderLE2(){
    int valor = (int) sliderLE2.getValue();

    estudante[1].setVelAbsorver(valor);
  }

  /************************************************************************
   * Metodo: sliderLE3
   * Funcao: controla a velocidade de absorcao do estudante3.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void sliderLE3(){
    int valor = (int) sliderLE3.getValue();

    estudante[2].setVelAbsorver(valor);
  }

  /************************************************************************
   * Metodo: sliderLE4
   * Funcao: controla a velocidade de absorcao do estudante4.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void sliderLE4(){
    int valor = (int) sliderLE4.getValue();

    estudante[3].setVelAbsorver(valor);
  }

  /************************************************************************
   * Metodo: sliderLE5
   * Funcao: controla a velocidade de absorcao do estudante5.
   * Parametros: void
   * Retorno: void
   ***********************************************************************/
  @FXML
  public void sliderLE5(){
    int valor = (int) sliderLE5.getValue();

    estudante[4].setVelAbsorver(valor);
  }

}//fim classe
