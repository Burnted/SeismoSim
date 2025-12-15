"""A simple script for myself to more easily create presets"""

file_name = input("Enter the name of the preset file (without extension): ")
file_path = f"/Users/caedenmitchell/Documents/GitHub/SeismoSim/resources/{file_name}.txt"

planet_radius = int(input("Enter planetary radius in km: ")) # scaling factor for complex presets
mode = input("Enter mode (simple or complex): ").lower() # Simple: layer_num = medium_count, Complex: layer_num = planet_radius
medium_count = 0 # number of mediums. mantle, core, etc.

layer_parameters_dict = {}
"""
Format:
"medium_num": {
        "layer_height": value,
        "v_i": value,
        "v_f": value,
        }
"""

def get_medium_parameters(medium_index):
    print(f"Configuring parameters for medium {medium_index}:")
    layer_height = int(input("  Enter layer height: "))
    v_i = float(input("  Enter initial velocity (v_i): "))
    v_f = float(input("  Enter final velocity (v_f): "))
    return {
        "layer_height": layer_height,
        "v_i": v_i,
        "v_f": v_f,
    }

def create_mediums():
    global medium_count
    print("Specify your mediums (mantle, outer core, inner core, etc.)")
    medium_count = int(input("Enter the number of mediums: "))
    total_height = 0
    for i in range(medium_count):
        layer_parameters_dict[i] = get_medium_parameters(i)
        total_height += layer_parameters_dict[i]["layer_height"]
    if total_height < planet_radius:
        layer_parameters_dict[medium_count-1]["layer_height"] += planet_radius - total_height
        print(f"Total layer height less than radius, set last layer to height of {layer_parameters_dict[medium_count-1]["layer_height"]}")
    elif total_height > planet_radius:
        layer_parameters_dict[medium_count-1]["layer_height"] -= total_height - planet_radius
        print(f"Total layer height more than radius, set last layer to height of {layer_parameters_dict[medium_count-1]["layer_height"]}")

create_mediums()

with open(file_path, "w") as preset_file:
    preset_file.write("# Preset Configuration File\n")
    preset_file.write(f"# layer type, velocity(km/s), radius(km)\n")

    if mode == "simple" or mode == "s":
        for medium in range(medium_count):
            v = layer_parameters_dict[medium]["v_i"]
            r = planet_radius if medium == 0 else planet_radius - layer_parameters_dict[medium-1]["layer_height"]
            preset_file.write(f"{medium},{v},{r}\n")
    elif mode == "complex" or mode == "c":
        dr = 0
        for medium in range(medium_count):
            v_i = layer_parameters_dict[medium]["v_i"]
            v_f = layer_parameters_dict[medium]["v_f"]
            r_i = planet_radius if medium == 0 else planet_radius - dr
            dr += layer_parameters_dict[medium]["layer_height"]
            layer_height = layer_parameters_dict[medium]["layer_height"]
            dv = (v_f - v_i) / layer_height
            for layer in range(layer_height):
                v = v_i + dv * layer
                r = r_i - layer
                preset_file.write(f"{medium},{v},{r}\n")