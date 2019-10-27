package model;

import java.lang.Thread;
import controller.Controller;
import java.util.concurrent.Semaphore;
import controller.Controller;


/******************************************************************
 * Classe: Escritor
 * Funcao: Classe reponsavel por controlar todas as
 *         acoes dos escritores
 *****************************************************************/
public class Escritor extends Thread{
  private int id;
  private Controller controller;
  private int velProduzir = 2000;
  private int velEscrever = 2000;
  private boolean acessandoRC = false; //  caso a Thread seja desativada enquanto estiver dentro da RC

  public Escritor(int id, Controller controller){
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
        
        produzirRecurso(); // simula a  producao (proucao da equacao)
        controller.animacaoIntencaoEntrarRCEscritor(this.id); // muda as cores para amarelo

        controller.downMutex(); // entra RC
        controller.animacaoDentroRCEscritor(this.id); // muda das cores para vermelho

        this.acessandoRC = true;

        controller.acessarRCEscritor(this.id); // altera a RC (escreve quadro)
        acessandoAreaCompartilhada(); // simula o tempo de acesso a RC

        controller.upMutex(); // sai RC
        this.acessandoRC = false;
        controller.animacaoForaRCEscritor(this.id); // muda as cores para verde
    
      }catch(Exception e){
        e.printStackTrace();
      }

    }//fim while
  }//fim método run

  /***************************************************************************
   * Metodo: acessandoAreaCompartilhada
   * Funcao: coloca a thread para dormir durante um tempo variavel, simulando
   *         a escrita no quadro
   * Parametros: void
   * Retorno: void
   **************************************************************************/
  public void acessandoAreaCompartilhada() throws Exception{
    sleep(velEscrever);
  }

  /***************************************************************************
   * Metodo: produzirRecurso
   * Funcao: coloca a thread para dormir durante um tempo variavel, simulando
   *         a producao de equacoes
   * Parametros: void
   * Retorno: void
   **************************************************************************/
  public void produzirRecurso() throws Exception {
    sleep(velProduzir);
  }

  /***************************************************************************
   * Metodo: setVelProduzir
   * Funcao: seta a velocidade de producao de equacoes
   * Parametros: int
   * Retorno: void
   **************************************************************************/
  public void setVelProduzir(int valor){
    this.velProduzir = (200 * valor);
  }

  /***************************************************************************
   * Metodo: setVelEscrever
   * Funcao: seta a velocidade de escrita das equacoes no quadro
   * Parametros: int -> velocidade
   * Retorno: void
   **************************************************************************/
  public void setVelEscrever(int valor){
    this.velEscrever = (200 * valor);
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
