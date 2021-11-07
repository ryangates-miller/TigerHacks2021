import static java.lang.Math.*;
import static java.lang.Object.*;
import java.util.*;



public class KeplerOrbitLocation{

public static void main(String[] args){
        

double eccen = 0.0167;//the numeric eccentricity of orbit (this value should be between 0 and 1)
double peri = 309;            //time since periapsis (this value was calculated in days from Jan 02, 2021)
int i = 4;                   //iterations to converge on eccentric anomoly
double T = 365.256;           //the period of orbit (this value is currently in days)
double semiAxis = 1.000001018;//the semi-major axis of orbit in AU

KeplerOrbitLocation myKep = new KeplerOrbitLocation();
Point point1 = myKep.getOrbitalPosition(eccen,peri,T,i,semiAxis);
System.out.print(point1.x + ", " + point1.y);

    
}

	public static class Point
	{
		public double x;
		public double y;
		
		public Point(double x, double y)
		{
			this.x = x;
			this.y = y;
		}
        
        //converts from angular to cartesian coordinates
		public Point Cartify()
		{
			return new Point((x * cos(y)), 
				(x * sin(y)));
		}	
	}
	
//Finds orbital position for a time elapsed since periapsis
	public static Point getOrbitalPosition( double numEcc, double peri,
			double T,  int i,double semiAxis)
	{
	    double mean = MeanAnomaly(T, peri);
	    double eccAn = EccAnomaly(numEcc, i,mean);
	    double theta = TrueAnomaly( eccAn, numEcc);
	    double r = LenRadius(semiAxis, numEcc, theta);
		return new Point(r, theta).Cartify();
	}


	//Calculates the eccentric anomoly, which is an angle of the satalite with relation to the Cartesian origin.
    //The cartesian origin will be halfway between the foci of the orbital ellipse.
	public static double EccAnomaly(double eccentricity, int iterations, double meanAnomaly)
	{
	    double numer;
		double temp = meanAnomaly;
		
		for (int i = 0; i < iterations; i++)
		{
			numer = meanAnomaly - eccentricity * temp * cos(temp) + eccentricity * sin(temp);
			temp = numer / (1 - eccentricity * cos(temp));
		}

		return temp;
	}

	//mean anomaly is the fraction of an elliptical orbit's period that has elapsed 
	//since the orbiting body passed periapsis, expressed as an angle 

	public static double MeanAnomaly(double T, double timePassed)
	{
		return (2*PI) / T * timePassed;
	}

    //calculates true anomoly, which is an angle to the main foci
	public static double TrueAnomaly( double eccAnomaly, double ecce)
	{
		return 2 * atan(sqrt((1 + ecce) / (1 - ecce))
				* tan(eccAnomaly / 2));
	}

    //get radius vector length
	public static double LenRadius(double semiAxis, double eccentricity, double theta)
	{
		double numer = semiAxis * (1 - (eccentricity*eccentricity));
		double denom = 1 + eccentricity * cos(theta);

		return numer / denom;
	}
}
