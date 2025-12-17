import pandas as pd

csv_file_name = input("Enter the name of the CSV file (without extension): ")
csv_file_path = f"/Users/caedenmitchell/Documents/GitHub/SeismoSim/resources/{csv_file_name}.csv"

output_file_name = input("Enter the name of the preset file (without extension): ")
output_file_path = f"/Users/caedenmitchell/Documents/GitHub/SeismoSim/resources/{output_file_name}.txt"

items = pd.read_csv(csv_file_path) if csv_file_name else pd.DataFrame()

planet_radius = float(items["Depth"].values[-1].split(" to ")[1])
medium_count = int(items["Layer"].values[-1]) + 1 # number of mediums. mantle, core, etc.
print(f"Planetary Radius: {planet_radius} km, Medium Count: {medium_count}")


with open(output_file_path, "w") as preset_file:
    preset_file.write("# Preset Configuration File\n")
    preset_file.write(f"# layer type, velocity(km/s), radius(km)\n")

    dr = 0
    for medium in range(medium_count):
        v_i = float(items["P-Wave V"].values[medium].split(" to ")[0])
        v_f = float(items["P-Wave V"].values[medium].split(" to ")[1])
        r_i = planet_radius if medium == 0 else planet_radius - dr
        layer_height = int(items["Depth"].values[medium].split(" to ")[1]) - int(items["Depth"].values[medium].split(" to ")[0])
        dr += layer_height
        dv = (v_f - v_i) / layer_height
        for layer in range(layer_height):
            v = v_i + dv * layer
            r = r_i - layer
            preset_file.write(f"{medium},{v},{int(r)}\n")


