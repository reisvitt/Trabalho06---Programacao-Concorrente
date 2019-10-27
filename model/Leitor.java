package model;

import java.lang.Thread;
import controller.Controller;
import java.util.concurrent.Semaphore;
import controller.Controller;
  

/******************************************************************
* Classe: Escritor
* Funcao: Classe reponsavel por controlar todas as
*         acoes dos leitores
*****************************************************************/
public class Leitor extends Thread{
    private Controller controller;
    private int id;
    private int velAbsorver = 2000;
    private int velVizualizando = 2000;
    private boolean acessandoRC = false; //  caso a Thread seja desativada enquanto estiver dentro da RC

  public Leitor(int id, Controller controller){
    this.controller = controller;
    this.id = id;
  }

  /***************************************************************************
   * Metodo: run
   * Funcao: orquestra as acoes da Thread;
   * Parametros: void
   * Retorno: void
   **************************************************************************/
  @Override
  public void run(){
    while(true){
      try{
        controller.animacaoIntencaoEntrarRCLeitor(this.id); // muda as cores para amarelo
        
        controller.acessarRCLeitor(this.id); // entra RC
        this.acessandoRC = true;

        controller.animacaoDentroRCLeitor(this.id); // muda as cores para vermelho

        vizualizando(); // simula o tempo de acesso a RC (leitura do quadro)

        controller.deixarRCLeitor(this.id); // sai RC
        controller.animacaoForaRCLeitor(this.id); // muda as cores para verde
        this.acessandoRC = false;

        absorverRecurso(); // simula tempo de absorcao do conhecimento

      }catch(Exception e){
        e.printStackTrace();
      }

    }//fim while
  }//fim método run

  /***************************************************************************
   * Metodo: absorverRecurso
   * Funcao: coloca a thread para dormir durante um tempo variavel, simulando
   *         a absorcao no conhecimento
   * Parametros: void
   * Retorno: void
   **************************************************************************/
  public void absorverRecurso() throws Exception{
    sleep(velAbsorver);
  }

  /***************************************************************************
   * Metodo: vizualizando
   * Funcao: coloca a thread para dormir durante um tempo variavel, simulando
   *         a vizualizacao no conhecimento;
   * Parametros: void
   * Retorno: void
   **************************************************************************/
  public void vizualizando() throws Exception {
    sleep(velVizualizando);
  }
  
  /***************************************************************************
   * Metodo: setVelAbsorver
   * Funcao: seta a velocidade de absorcao das equacoes
   * Parametros: int
   * Retorno: void
   **************************************************************************/
  public void setVelAbsorver(int valor){
    this.velAbsorver = (200 * valor);
  }

  /***************************************************************************
   * Metodo: setVelVizualizar
   * Funcao: seta a velocidade de vizualizacao das equacoes
   * Parametros: int
   * Retorno: void
   **************************************************************************/
  public void setVelVizualizar(int valor){
    this.velVizualizando = (200 * valor);
  }

  /***************************************************************************
   * Metodo: setAcessandoRC
   * Funcao: seta se esta acessando a RC ou nao
   * Parametros: boolean
   * Retorno: void
   **************************************************************************/
  public void setAcessandoRC(boolean valor){
    this.acessandoRC = valor;
  }

  /***************************************************************************
   * Metodo: getAcessandoRC
   * Funcao: retorna se esta acessando a RC ou nao
   * Parametros: void
   * Retorno: boolean
   **************************************************************************/
  public boolean getAcessandoRC(){
    return this.acessandoRC;
  }

}//fim classe
