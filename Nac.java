package br.com.fiap.nac;

import java.text.DecimalFormat;

public class Nac {

	//declarações globais
	
	static int[] indCD=new int[3];
	
	static String[] campoP=new String[] {"Cor","Preço","Volume (m³)","Peso (KG)"};	
	static double[][] prod=new double[][] {
		{1,15,0.012,3},
		{2,16,0.010,2.4},
		{3,17,0.011,3.6}
	};
	
	static String[] campo=new String[] {"Data","Cliente","Latitude","Longitude",
			"Cor 1","Cor 2","Cor 3","Prioridade"};
	static String[][] listaVenda=new String[][] {
		{"10/10/2017","Lojas ABC","-10.1510","-36.3304","25","18","10", "PRAZO"},
		{"10/10/2017","Lojas Venda Certa","-21.4848","-51.5355","15","32","19","FRETE"},
		{"10/10/2017","Armarinhos Ferreira","-27.6349","-52.2737","35","56","30","PRAZO"},
	};
	
	static String[] campoC=new String[] {"Tipo","Nome","Latitude","Longitude"};
	static String[][] listaCD=new String[][] {
		{"CD","São Paulo","-23.5489","-46.6388"},
		{"CD","Porto Alegre","-30.0277","-51.2287"},
		{"CD","Salvador","-12.9704","-38.5124"},
		{"CD","Recife","-8.05428","-34.8813"},
		{"CD","Goiânia","-16.6799","-49.255"},
		{"CD","Manaus","-3.10719","-60.0261"}
	};
	
	public static void main(String[] args) {
		
		DecimalFormat fm=new DecimalFormat("####.##");
		double[] valor=valorPedido(prod,listaVenda);
		double[] frete=valorFrete();
		
		System.out.println("-----VALOR DOS PRODUTOS------");
		for (int i=0;i<valor.length;i++) {
			System.out.println("O valor dos produtos do cliente "+listaVenda[i][1]+" é de R$"+(valor[i]));
		}
		System.out.println("");
		
		System.out.println("-------PRAZOS-------");
		prazoEntrega();
		System.out.println("");
		
		System.out.println("------FRETES-------");
		for (int i=0;i<frete.length;i++){
			System.out.println("Frete para "+listaVenda[i][1]+" é de R$"+fm.format(frete[i]));
		}
		System.out.println("");
		
		System.out.println("-------VALOR TOTAL-------");
		for (int i=0;i<frete.length;i++) {
			System.out.println("Valor total da compra: R$"+fm.format(frete[i]+valor[i]));
		}
		
	}
	
	//Métdos
	
	//Método para calcular o peso da carga
	static double[] peso(double[][] prod, String[][] listaVenda){
		
		double[] peso=new double[3];
		
		int ind=0;
		
		for (int i=0;i<peso.length;i++) {
			ind=0;
			for (int j=4;j<7;j++) {
				peso[i]=prod[ind][3]*Double.parseDouble(listaVenda[i][j]);
				ind++;
			}					
		}
		
		return peso;
		
	}
	
	//Método para calculara o valor da venda
	static double[] valorPedido(double[][] prod, String[][] listaVenda){
	
	double[] valor=new double[3];
	int ind=0;
	
	for (int i=0;i<valor.length;i++) {
		ind=0;
		for (int j=4;j<7;j++) {
			valor[i]+=prod[ind][1]*Double.parseDouble(listaVenda[i][j]);
			ind++;
		}					
	}
	
	return valor;
	}//fim do método
	
	//Método Para calcular o valor do frete
	static double[] valorFrete() {
		
		double retorno[]=calcDist(listaCD,listaVenda);
		double peso[]=peso(prod,listaVenda);
		double frete[]=new double[3];
		
		for (int i=0;i<retorno.length;i++) {
			
			if (retorno[i]>=500) {
				frete[i]=(Math.ceil(retorno[i]/300))*0.2*peso[i]+50;
			}else if (retorno[i]<500&&retorno[i]>=100){
				frete[i]=(Math.ceil(retorno[i]/50))*0.04*peso[i]+30;
			}else{
				frete[i]=(Math.ceil(retorno[i]/25))*0.04*peso[i]+25;
			}//fim do if
			
		}//fim do for
		
		return retorno;
	}//fim do método
	
	//Calculo da conversão lat/long para KM
	//VERIFICAÇÃO QUAL CD É MAIS PRÓXIMO
	static double[] calcDist(String[][] listaCD,String[][] listaVenda){
		
		double[] retorno=new double[3];
		double d=0;
		
		//pegando a lat/long do cliente q fes o pedido
		for (int i=0;i<listaVenda.length;i++) {
			d=0;
			//esse será usado para controlar a lista de centros de distribução
			for (int j=0;j<listaCD.length;j++) {
				
				d=Math.sqrt(
						Math.pow(
								(Double.parseDouble(listaVenda[i][2])-Double.parseDouble(listaCD[j][2])), 2)+
						Math.pow(
								(Double.parseDouble(listaVenda[i][3])-Double.parseDouble(listaCD[j][3])), 2)
						)*109;
				
				if(retorno[i]==0) {
					retorno[i]=d;
					indCD[i]=j;
				}
				if(retorno[i]>d) {
					retorno[i]=d;
					indCD[i]=j;
				}
				
			}//fim for interno
		}//fim for externo
				
		return retorno;
	}//fim do método
	
	//Método para determinar o prazo de entrega
	static void prazoEntrega() {
		
		double[] km=calcDist(listaCD,listaVenda);
		
		int[] prazoD=new int[3];
		
		for (int i=0;i<km.length;i++){
			if (km[i]<500){
				prazoD[i]=5;
			}else if (km[i]<800){
				prazoD[i]=8;
			}else{
				prazoD[i]=10;
			}
			
			System.out.println("Venda para o cliente "+
			listaVenda[i][1]+" Chegara em "+prazoD[i]+" dias.");
			System.out.println("Entrega será feita pelo Centro de Distribuição de "+listaCD[indCD[i]][1]);
			System.out.println("");
		}//fim for
		
	}//fim do método
	
}
