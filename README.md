# **Kotlin Seismic Wave Simulator (Swing)**

This project is an interactive **2D ray-tracing simulator** written in **Kotlin** using **Java Swing**.  
It primitively models seismic waves within the earth using **Snell’s Law**, **refraction**, **reflection**, and change in velocities between mediums. 
The user can **drag the initial ray with the mouse**, and the simulation updates in real time.

---

## **Preview**

<!-- Image 1 -->
![alt text](https://github.com/Burnted/SeismoSim/blob/main/media/screen1.png)

<br>

<!-- Image 2 -->
![alt text](https://github.com/Burnted/SeismoSim/blob/main/media/screen2.png)

---

## **Features**

- Physics-accurate **Snell’s Law** refraction
- Presets for different planetary bodies
  - Currently includes an Earth and a simple preset
- Support for different seismic wave types (P-waves and S-waves)
- **Total Internal Reflection (TIR)** automatically handled
- Rays continue bouncing/refracting until a bounce limit is reached
- Real-time ray movement via **mouse control**
- Implements vector geometry for angle calculations
- Clean class structure:
    - `Vector2` – vector math utilities
    - `Ray` – origin + direction
    - `Circle` – refractive boundary geometry
    - `Intersections` – geometric intersection routines
    - `RayTracer` – performs recursive refraction/reflection
---
