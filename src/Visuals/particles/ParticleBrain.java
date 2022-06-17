package Visuals.particles;

import Visuals.engine.graphics.Loader;
import Visuals.entities.Camera;
import org.lwjglx.util.vector.Matrix4f;

import java.util.*;

public class ParticleBrain {

    private static Map<ParticleTexture, List<Particle>> particles = new HashMap<ParticleTexture, List<Particle>>();
    private static ParticleRenderer renderer;

    public static void init(Loader loader, Matrix4f projectionMatrix){
        renderer = new ParticleRenderer(loader, projectionMatrix);
    }

    public static void update(Camera camera){
        Iterator<Map.Entry<ParticleTexture, List<Particle>>> hashIterator = particles.entrySet().iterator();
        while(hashIterator.hasNext()){
            List<Particle> list = hashIterator.next().getValue();
            Iterator<Particle> iterator = list.iterator();
            while(iterator.hasNext()){
                Particle p = iterator.next();
                boolean exists = p.update(camera);
                if(!exists){
                    iterator.remove();
                    if(list.isEmpty()){
                        hashIterator.remove();
                    }
                }
            }
            Sort.sortHighToLow(list);
        }

    }

    public static void renderParticles(Camera camera){
        renderer.render(particles, camera);

    }

    public static void cleanUp(){
        renderer.cleanUp();

    }

    public static void addParticle(Particle particle){
        List<Particle> list = particles.get(particle.getTexture());
        if (list ==null){
            list = new ArrayList<Particle>();
            particles.put(particle.getTexture(),list);

        }
        list.add(particle);
    }
}
