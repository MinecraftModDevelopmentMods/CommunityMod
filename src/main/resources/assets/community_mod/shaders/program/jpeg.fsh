// Disclaimer: I actually have no idea how JPEG works so shhh

#version 120

uniform sampler2D DiffuseSampler;

varying vec2 texCoord;
varying vec2 oneTexel;

const int QUAD_SIZE = 64;
const int SAMPLE_STEP = 16;

const int SAMPLE_COUNT = (QUAD_SIZE * QUAD_SIZE) / (SAMPLE_STEP * SAMPLE_STEP);
const float COLOR_GRANULARITY = 0.3;

const int NOISE_SIZE = 8;
const float NOISE_FACTOR = 1.0;

float rand(vec2 n) {
    return fract(sin(dot(n, vec2(12.9898, 4.1414))) * 43758.5453);
}

vec4 sample_noise(vec2 coord, vec4 color) {
    vec2 texel = coord / oneTexel;
    vec2 noiseCoord = vec2(floor(texel.x / NOISE_SIZE), floor(texel.y / NOISE_SIZE));

    return vec4(
        rand(noiseCoord * vec2(-6512.0, 4517.0) * color.xy),
        rand(noiseCoord * vec2(5321.0, -4131.0) * color.yz),
        rand(noiseCoord * vec2(210.0, -63.0) * color.xz),
        0.0
    );
}

vec2 tex_to_quad(vec2 texCoord) {
    vec2 texel = texCoord / oneTexel;
    return vec2(floor(texel.x / QUAD_SIZE), floor(texel.y / QUAD_SIZE));
}

vec2 tex_in_quad(vec2 quad, int ox, int oy) {
    vec2 texel = vec2(quad.x * QUAD_SIZE, quad.y * QUAD_SIZE) + vec2(ox, oy);
    return texel * oneTexel;
}

vec4 average_color_in(vec2 quad) {
    vec4 total = vec4(0, 0, 0, 0);
    for (int y = 0; y < QUAD_SIZE; y += SAMPLE_STEP) {
        for (int x = 0; x < QUAD_SIZE; x += SAMPLE_STEP) {
            total += texture2D(DiffuseSampler, tex_in_quad(quad, x, y));
        }
    }
    return total / SAMPLE_COUNT;
}

float compress_color_component(float delta, float noise) {
    float compressed = floor(delta / COLOR_GRANULARITY) * COLOR_GRANULARITY;
    float lost_quality = delta - compressed;
    return compressed + noise * lost_quality * NOISE_FACTOR;
}

void main() {
    vec4 color = texture2D(DiffuseSampler, texCoord);

    vec2 quad = tex_to_quad(texCoord);
    vec4 average_color = average_color_in(quad);
    vec4 noise = sample_noise(texCoord, average_color);

    vec4 color_delta = color - average_color;

    gl_FragColor = vec4(
        clamp(average_color.x + compress_color_component(color_delta.x, noise.x), 0.0, 1.0),
        clamp(average_color.y + compress_color_component(color_delta.y, noise.y), 0.0, 1.0),
        clamp(average_color.z + compress_color_component(color_delta.z, noise.z), 0.0, 1.0),
        1.0
    );
}
