//This class is to represent basic characteristics of a body
package OrbitCalculations;

public class Body {

    public static Body KERBOL = new Body("Kerbol",1.1723328*Math.pow(10,18),-1,261600000,94672,-1);

    static Body KERBIN =  new Body("Kerbin",3.5315984*Math.pow(10,12),
            70000,600000,3431,84159271,
            13600000000f,9284.5,true,KERBOL);

    public static Body JOOL = new Body("Jool",2.8252800*Math.pow(10,14),140000,6000000,
            9704,2.4559852*Math.pow(10,9),68773560320f,((3927+4341)/2),true,KERBOL);

    public static Body MUN = new Body("Mun", (6.5138398*(Math.pow(10,10))),
            10000,200000,807.08,2492559.6,12000000,542,false,KERBIN);

    public static Body MINMUS = new Body("Minmus", 1.7658000*Math.pow(10,9),
            6000,60000,242.61,2247428.8,47000000,274,false,KERBIN);

    public static Body MOHO = new Body("Moho",1.6860938*Math.pow(10,11),
            30000,250000,1161.41,9646663,5263138304f,((12186+18279)/2),false,KERBOL);

    public static Body DUNA = new Body("Duna",3.013621*Math.pow(10,11),
            60000,320000,1372.41,47921949,20726155264f,(7147+7915)/2,true,KERBOL);

    public static Body DRES = new Body("Dres",2.1484489*Math.pow(10,10),
            30000,138000,558,32832840,40839000000f,(4630+6200)/2,false,KERBOL);

    public static Body EELOO = new Body("Eeloo",7.4410815*Math.pow(10,10),
            20000,210000,841.83,1.1908294*Math.pow(10,8),90118820000f,(2764+4706)/2,false,KERBOL);
    public static Body[] BODIES = {KERBOL,MOHO,KERBIN,MUN,MINMUS,DUNA,JOOL,EELOO};

    private String name;
    private double gravitationalConstant;
    private double lowOrbitAltitude;
    private double equitorialRadius;
    private double escapeVelocity;
    private double spherOfInfluence;
    private double semiMajorAxis;
    private double orbitalVelocity;
    private boolean atmosphere;

    private Body parentBody;

    public Body(){

    }

    public Body(String name, double gm, double lowOrbitAlt,
                double equitorialRadius, double escapeVelocity, double spherOfInfluence){
        this.name = name;
        this.gravitationalConstant = gm;
        this.lowOrbitAltitude = lowOrbitAlt;
        this.equitorialRadius = equitorialRadius;
        this.escapeVelocity = escapeVelocity;
        this.spherOfInfluence = spherOfInfluence;
    }

    public Body(String name, double gm, double lowOrbitAlt,
                double equitorialRadius, double escapeVelocity, double spherOfInfluence, float semiMajorAxis, double orbitalVelocity,boolean atm, Body parentBody){
        this.name = name;
        this.gravitationalConstant = gm;
        this.lowOrbitAltitude = lowOrbitAlt;
        this.equitorialRadius = equitorialRadius;
        this.escapeVelocity = escapeVelocity;
        this.spherOfInfluence = spherOfInfluence;
        this.setSemiMajorAxis(semiMajorAxis);
        this.setOrbitalVelocity(orbitalVelocity);
        this.setParentBody(parentBody);
        this.atmosphere = atm;
    }

    public void setParentBody(Body parentBody){
        this.parentBody = parentBody;
    }

    public void setSemiMajorAxis(float semiMajorAxis){
        this.semiMajorAxis = semiMajorAxis;
    }

    public void setOrbitalVelocity(double orbitalVelocity){
        this.orbitalVelocity = orbitalVelocity;
    }

    public boolean checkName(String name){
        return this.name.matches(name);
    }

    public String getName(){
        return this.name;
    }

    public double calculateLowOrbitVelocity(){
        //V = sqrt(GM/radiusOfOrbit)
        double requiredVelocity = (int)(1.1*(Math.sqrt(this.gravitationalConstant/(this.equitorialRadius +this.lowOrbitAltitude))+5)/10)*10;
        return requiredVelocity;

    }
    public double calculateLowOrbitVelocityTakeOff(){
        //V = sqrt(GM/radiusOfOrbit)
        double requiredVelocity = (int)(1.1*(Math.sqrt(this.gravitationalConstant/(this.equitorialRadius +this.lowOrbitAltitude))+5)/10)*10;
        if(this.atmosphere){
            requiredVelocity+=1000;
        }
        return requiredVelocity;

    }

    public double getGM(){
        return this.gravitationalConstant;
    }

    public Body getParentBody(){
        return parentBody;
    }

    public double getSemiMajorAxis(){
        return this.semiMajorAxis;
    }
    public double calculateTransferOrbitTo(Body destinationBody){
        //Calculate velocit needed AFTER escape velocity
        // v = sqrt(GM*(2/r-1/a))
        double r = getSemiMajorAxis();
        //double r = 40839000000f;
        //double a = (13600000000f+40839000000f)/2;
        double a = (this.semiMajorAxis+destinationBody.getSemiMajorAxis())/2;
        double gm = getParentBody().getGM();
        System.out.println("GM: "+gm+"R: "+r+" A: "+a);
        double v = Math.round(Math.sqrt(gm*((2/r)-(1/a))));
        double v0 = v;
        //Start body orbatle velocity difference
        System.out.println("Total Transfer to orbit V: "+v);
        System.out.println("Current Body Orbatal Velocity: "+this.orbitalVelocity);
        if(v<this.orbitalVelocity){

            v = this.orbitalVelocity-v;
            System.out.println("V LESS THAN OV VELOCITY DIFFERENCE :"+(v));
        }
        else{
            v =v- this.orbitalVelocity;
           System.out.println("VELOCITY DIFFERENCE :"+(v));
        }

        v = pythagoreanTherom(this.escapeVelocity,v);
        System.out.println("Transfer to orbit V: "+v);
        System.out.println("INJECTION BURN: "+calculateInsersionToOrbit(v0,destinationBody));
        return v;//+calculateLowOrbitVelocity();
    }

    public double getSoe(){
        return this.spherOfInfluence;
    }
    private double calculateInsersionToOrbit(double v, Body destinationBody){
        double vsoi = destinationBody.getEscapeVelocity();
        double v0 = v;
        double gm = destinationBody.getGM();
        double rsoi = destinationBody.getSoe();
        return v0-vsoi;
        //return Math.sqrt((vsoi*vsoi)+(2*v0*v0)-(2*(gm/rsoi)-v0));

    }

    private double pythagoreanTherom(double orbitalVelocity,double requiredDV){
        //v2 = v_esc2 + v_inf2. So v
        return Math.round(Math.sqrt(Math.pow(orbitalVelocity,2)+Math.pow(requiredDV,2)));
    }

    public double lowOrbitToEscapeVelocity(){
        return Math.round(getEscapeVelocity()-calculateLowOrbitVelocity());
    }

    public double getEscapeVelocity(){
        return this.escapeVelocity;
    }

    public double escapeVelocityToLowOrbit(){
        return Math.round(getEscapeVelocity()-calculateLowOrbitVelocity());
    }

    public double calculateLanding(){
        if(this.atmosphere){
            return 0;
        }
        else{
            return this.calculateLowOrbitVelocity();
        }
    }

    public static void main(String[] argS){
        Body kerbol = new Body("Kerbol",1.1723328*Math.pow(10,18),-1,261600000,94672,-1);

        Body kerbin =  new Body("Kerbin",3.5315984*Math.pow(10,12),70000,600000,3431,84159271);
        kerbin.setSemiMajorAxis(13599840256f);
        kerbin.setOrbitalVelocity(9284.5);
        kerbin.setParentBody(kerbol);

        Body jool = new Body("Jool",2.8252800*Math.pow(10,14),140000,6000000,
                9704,2.4559852*Math.pow(10,9));
        jool.setSemiMajorAxis(68773560320f);
        jool.setOrbitalVelocity((3927+4341)/2);
        jool.setParentBody(kerbol);

        Body mun = new Body("Mun", (6.5138398*(Math.pow(10,10))),10000,200000,807.08,2492559.6);
        mun.setParentBody(kerbin);
        mun.setSemiMajorAxis(12000000);
        mun.setOrbitalVelocity(542);

        Body minmus = new Body("Minmus", 1.7658000*Math.pow(10,9),6000,60000,242.61,2247428.8);
        minmus.setParentBody(kerbin);
        minmus.setSemiMajorAxis(47000000);
        minmus.setOrbitalVelocity(274);

        Body moho = new Body("Moho",1.6860938*Math.pow(10,11),30000,250000,1161.41,9646663);
        moho.setParentBody(kerbol);
        moho.setSemiMajorAxis(5263138304f);
        moho.setOrbitalVelocity((12186+18279)/2);

        Body testBody = DRES;
        System.out.println("START: "+testBody.getName());

        System.out.println("Low Orbit: "+testBody.calculateLowOrbitVelocityTakeOff());
        System.out.println("Escape: "+testBody.lowOrbitToEscapeVelocity());
        System.out.println("Transfer: "+testBody.calculateTransferOrbitTo(DUNA));

        System.out.println("To Low Orbit: "+(DUNA.lowOrbitToEscapeVelocity()));
        System.out.println("Capture to Low Orbit: "+(DUNA.calculateLowOrbitVelocity()));


    }
}
