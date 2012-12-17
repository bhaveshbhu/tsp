package tsp;
/*
 * @author bhavesh sharma
 */
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
public class TSP {
 static int alpha =3;
 static int beta =2;
    static double rho = 0.01;  //periphome decreasing fac 
    static double q =100;    // periphome increasing fa
    static double w = 0.01;
    static double phe=0.01;
 private int vertexCount;
      public static void main(String[] args) throws IOException {
      int[][] weight = new int[42][42];
      int num_of_ants=1; 
      int max_time=10000;
      int n;
        System.out.println("ENTER THE NO OF VERTEX");
       // Scanner re = new Scanner(System.in);
       // n=re.nextInt();
        n=42;
       // System.out.println(n);
       //        for (int i=0; i<n; ++i) {
//  for (int j=0; j<n; j++) {
//      System.out.print(i+" ") ; System.out.print(j+ " ");
//       weight[i][j] = re.nextInt();
//  }
//  }
          FileReader fin = new FileReader("C:/Users/lenovo/Documents/javaprog/dantzig42_d.txt");
  
 Scanner src = new Scanner(fin);
for(int i=0;i<n;++i)
{
for(int j=0;j<n;j++)
{
weight[i][j]=src.nextInt();
System.out.print(weight[i][j]+"  ");
}
System.out.println("  ");
}
    fin.close();
    /*   TO PRINT DIST MATRIX  
         for (int i=0; i<weight.length; ++i) {
  for (int j=0; j<weight[i].length; ++j) {
   System.out.print(" " + weight[i][j]);
  }
  System.out.println("");
  }
         
  */
          int[][] ants = ant_initialize(num_of_ants,n);  
       show_ants(ants,weight);
       int best_index = best_path(ants,weight);
      double best_len = calculate_length(ants[best_index],weight);
              System.out.println("BEst Length is " + best_len);
              double[][] pheromones= initialize_pheromones(n);
              
              int time=0;
              
              while(time<max_time)
             {
                //  System.out.println(time);
              update_ants(ants,pheromones,weight,n);
              //update_pheromones(pheromones, ants, weight);
              int curr_best_path =  best_path(ants,weight);
//                     for (int i=0; i<weight.length; ++i) {
//  for (int j=0; j<weight[i].length; ++j) {
//   System.out.print(" " + pheromones[i][j]);
//  }
//  System.out.println("");
//  }
          //  local_update(pheromones, ants,weight); 
//                                  for (int i=0; i<weight.length; ++i) {
//  for (int j=0; j<weight[i].length; ++j) {
//   System.out.print(" " + pheromones[i][j]);
//  }
//  System.out.println("");
 // }
              global_update(pheromones,ants, curr_best_path, weight);
             double curr_best_len = calculate_length(ants[curr_best_path],weight);
             if (curr_best_len < best_len)
             {
           best_len = curr_best_len;
           best_index = curr_best_path;
             System.out.println("New best length of " + best_len + " found at time " + time);
             for (int j=0; j<n; ++j) {
      System.out.print(ants[best_index][j]+ "  ");
  }
         System.out.println("");}
          ++time;
              }
              
    } 

 static int[][] ant_initialize(int num_of_ants,int num_of_cities)
    { int begin;
    boolean[] begin_initialize = new boolean[num_of_cities]; 
    Arrays.fill(begin_initialize, Boolean.FALSE);
        Random random = new Random();
        int range = num_of_cities;
    int[][] ants=new int[num_of_ants][];
    for(int k=0;k<num_of_ants;k++)
    {
    begin = random.nextInt(range);
         while(begin_initialize[begin]==true)
       {
       begin = random.nextInt(range);
       }
       begin_initialize[begin]=true;
    ants[k]= rand_path(begin,num_of_cities);
    }
        return ants;
    }

    static int[] rand_path(int begin, int num_of_cities) {
        Random ra = new Random();
        
        int[] path = new int[num_of_cities];
        for(int i=0;i<num_of_cities;i++) {path[i]=i;}
        
        for(int i=0;i<num_of_cities;i++) {
            int range = num_of_cities-i;
        int r = ra.nextInt(range)+i;
        int tmp= path[r]; path[r]=path[i]; path[i]=tmp; 
        }
        
        for(int i=0;i<path.length;i++)
        {
        if(path[i]==begin) {int tmp =path[0]; path[0]=begin; path[i]=tmp; } }
        return path;
    }
static void show_ants(int[][] ants,int[][] dist)
{
for(int i=0;i<ants.length;++i)
{
        for(int j=0;j<ants[i].length;++j)
        {
       System.out.print(" " + ants[i][j]);
        }
        double dis = calculate_length(ants[i],dist);
        System.out.print("  Distance : " + dis);
      System.out.println(" ");  
}
}
    
    static double calculate_length(int[] ants,int[][] dist)
    {
        int i;
        double di=0; 
        for(i=0;i<ants.length-1;i++)
        {
        di=di+ dist[ants[i]][ants[i+1]];
        }
       return di;
    
    }
    
    static int best_path(int[][] ants,int[][] weight)
    {   int i=0,idx=0;
        double smallest_length = calculate_length(ants[0],weight);
        for(i=1;i<ants.length;++i)
        {
        double temp_length = calculate_length(ants[i],weight);
        if(temp_length<smallest_length)
        { smallest_length = temp_length;
        idx=i;
        }
        }
        return idx;
    }
    
    static double[][] initialize_pheromones(int num_of_cities)
    {
    double[][] pheromones = new double[num_of_cities][];
    for (int i = 0; i < num_of_cities; ++i) {
            pheromones[i] = new double[num_of_cities];
        }
    for (int i = 0; i < pheromones.length; ++i) {
            for (int j = 0; j < pheromones[i].length; ++j) {
            pheromones[i][j] = phe;
        }
        } 
    return pheromones;
    
    }
    static void update_ants(int[][] ants,double[][] pheromones, int[][] weight,int num_of_cities)
    { 
      int begin;
      boolean[] begin_initialize = new boolean[num_of_cities]; 
      Arrays.fill(begin_initialize, Boolean.FALSE);
        Random random = new Random();
        int range = num_of_cities;
    for(int k=0;k<ants.length;++k)
    {
          begin = random.nextInt(range);
       while(begin_initialize[begin]==true)
       {
       begin = random.nextInt(range);
       }
       begin_initialize[begin]=true;
    int[] new_path= build_path(k,begin,pheromones,weight,num_of_cities);
    ants[k] = new_path;
    }
   }
    
    static int[] build_path(int k,int begin,double[][] pheromones,int[][] weight,int num_of_cities)
    {   int i;
        int path[] = new int[num_of_cities] ;
        boolean[] visited = new boolean[num_of_cities];
        Arrays.fill(visited, Boolean.FALSE);
       // visited[num_of_cities] =false;
        path[0]=begin;
        visited[begin]=true;
        for(i=0;i<num_of_cities-1;++i)
        {int city_start = path[i];
        int next_city = get_next_city(k,weight,city_start,pheromones,visited,num_of_cities);
       // local_update(pheromones,ants,weight,city_start,next_city,k);
        path[i+1]=next_city;
          double decrease = (1.0 - rho) *pheromones[city_start][next_city];
            double increase =rho*phe;
         pheromones[city_start][next_city] = decrease + increase;
        visited[next_city]=true;
        }
        return path;
    
    }
  
    static int get_next_city(int k,int[][] weight,int city_start,double[][] pheromones,boolean[] visited,int num_of_cities)
    { int i,idx=0;
        double[] prob = new double[num_of_cities];
       // double sum = 0.0;
        double temp=0;
        for(i=0;i<prob.length;++i)
        {
        if(i==city_start) {
                prob[i]=0.0;
            }
        else if(visited[i]==true) {
                prob[i]=0.0;
            }
        else
            {
               double t = 1.0/weight[city_start][i];
          prob[i]= Math.pow(pheromones[city_start][i],beta)*Math.pow(t,alpha);
        
          if(prob[i]>temp) { temp = prob[i]; idx=i; }
//             if(prob[i]<0.0001) {
//                    prob[i]=0.0001;
//                }
//             else if(prob[i]>(Double.MAX_VALUE / (num_of_cities * 100)))
//             { prob[i] = Double.MAX_VALUE / (num_of_cities * 100);}
            }
        //sum+=prob[i];
        }
        return idx;
    }
//    static int get_next_city(int k,int[][] weight,int city_start,double[][] pheromones,boolean[] visited,int num_of_cities) 
//   { 
//       int j=0;
//        double[] probs = move_prob(k, city_start, visited, pheromones, weight,num_of_cities);
//double[] cumul = new double[probs.length + 1];
//cumul[0]=0;
//      for (int i = 0; i < probs.length; ++i) {
//            cumul[i + 1] = cumul[i] + probs[i];
//        } // consider setting cumul[cuml.Length-1] to 1.00
//Random r=new Random();
//      double p = r.nextDouble();
//
//      for (int i = 0; i < cumul.length - 1; ++i) {
//            if (p >= cumul[i] && p < cumul[i + 1]) {
//               j=i; break;
//          }
//       }
//   return j;
//  }
//    static double[] move_prob(int k, int city_start, boolean[] visited, double[][] pheromones, int[][] weight,int num_of_cities)
//    {
//    int i,idx=0;
//        double[] prob = new double[num_of_cities];
//       double sum = 0.0;
//        double temp=0;
//        for(i=0;i<prob.length;++i)
//        {
//        if(i==city_start) {
//                prob[i]=0.0;
//            }
//        else if(visited[i]==true) {
//                prob[i]=0.0;
//            }
//        else
//            {
//               double t = 1.0/weight[k][i];
//          prob[i]= Math.pow(pheromones[k][i],alpha)*Math.pow(t,alpha);
//        
//          if(prob[i]>temp) { temp = prob[i]; idx=i; }
//             // if(prob[i]<0.0001)
//            //  prob[i]=0.0001;
//            //  else if(prob[i]>(double.MaxValue / (num_of_cities * 100)))
//           //   { prob[i] = double.MaxValue / (num_of_cities * 100);}
//            }
//        sum+=prob[i];
//        }
//        double[] prob_1 = new double[num_of_cities];
//      for (i = 0; i < prob.length; ++i) {
//            prob_1[i] = prob[i] / sum;
//        } 
//      return prob_1;
//    }
    
    
//  static void local_update(double[][] pheromones, int[][] ants, int[][] weight) 
//  {
//   for (int i = 0; i < pheromones.length; ++i)
//      {
//        for (int j = i + 1; j < pheromones[i].length; ++j)
//        {
//             double decrease = (1.0 - rho) * pheromones[i][j];
//            double increase =0.0;
//            for (int k = 0; k < ants.length; ++k)
//          {
//            double length = calculate_length(ants[k],weight); // length of ant k trail
//              if (EdgeInTrail(i, j, ants[k]) == true) 
//               increase =increase+(q / length);
//           }
//             pheromones[i][j] = decrease + increase;
//            if (pheromones[i][j] < 0.0001)
//              pheromones[i][j] = 0.0001;
//            else if (pheromones[i][j] > 100000.0)
//              pheromones[i][j] = 100000.0;
//         pheromones[j][i] = pheromones[i][j];
//      }
//    }
//  
//  }
  
  static void global_update(double[][] pheromones,int[][] ants,int k, int[][] weight)
  {
       double length = calculate_length(ants[k], weight);
  for (int i = 0; i < pheromones.length; ++i)
      {
        for (int j = i + 1; j < pheromones[i].length; ++j)
        {
            double decrease = (1.0 - w) * pheromones[i][j];
            double increase = 0.0;
            if (EdgeInTrail(i, j, ants[k]) == true) increase = w*(q / length);

            pheromones[i][j] = decrease + increase;

            if (pheromones[i][j] < 0.0001)
              pheromones[i][j] = 0.0001;
            else if (pheromones[i][j] > 100000.0)
              pheromones[i][j] = 100000.0;

            pheromones[j][i] = pheromones[i][j];
        
        }
      } 
  }
  
     static boolean EdgeInTrail(int city_x, int city_y, int[] path) {
        int last_idx = path.length -1;
        int i,idx = 0; 
                for(i=0;i<path.length;++i) 
                { if(path[i]==city_x) {idx=i; break;}  }
                if(idx==0 && path[1]==city_y) {
             return true;
         }
      else if (idx == 0 && path[last_idx] == city_y) return true;
      else if (idx == 0) return false;
      else if (idx == last_idx && path[last_idx - 1] == city_y) return true;
      else if (idx == last_idx && path[0] == city_y) return true;
      else if (idx == last_idx) return false;
      else if (path[idx - 1] == city_y) return true;
      else if (path[idx + 1] == city_y) return true;
      else return false;
    }
}




