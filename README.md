# **Kotlin Seismic Wave Simulator (Swing)**

This project is an interactive **2D ray-tracing simulator** written in **Kotlin** using **Java Swing**.  
It primitively models seismic waves within the earth using **Snell’s Law**, **refraction**, **reflection**, and change in velocities between mediums. 
The user can **drag the initial ray with the mouse**, and the simulation updates in real time.

---

## **Preview**

<!-- Image 1 -->
![alt text](https://github.com/Burnted/SeismoSim/blob/main/media/Screenshot%202025-12-10%20at%201.10.39%E2%80%AFAM.png)

<br>

<!-- Image 2 -->
![alt text](https://github.com/Burnted/SeismoSim/blob/main/media/Screenshot%202025-12-10%20at%201.10.59%E2%80%AFAM.png)

---

## ** Features**

- Physics-accurate **Snell’s Law** refraction
- **Total Internal Reflection (TIR)** automatically handled
- Rays continue bouncing/refracting until a bounce limit is reached
- Supports **multiple refractive media** (circles with customizable indices)
- Real-time ray movement via **mouse control**
- Clean class structure:
    - `Vector2` – vector math utilities
    - `Ray` – origin + direction
    - `Circle` – refractive boundary geometry
    - `Intersections` – geometric intersection routines
    - `RayTracer` – performs recursive refraction/reflection
---
