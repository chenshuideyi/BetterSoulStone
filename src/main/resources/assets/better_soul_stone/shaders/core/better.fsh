#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform float Time;

in float vertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;
in vec4 normal;

out vec4 fragColor;

#define TAU 6.283185307179586476925286766559
#define PI 3.1415926535897932384626433832795
#define PID2 1.5707963267948966192313216916398

vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0/3.0, 1.0/3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

void main() {
    vec4 texcolor = texture(Sampler0, texCoord0);
    vec2 atlasSize = textureSize(Sampler0, 0);
    vec2 uv = texCoord0 * atlasSize;
    uv = floor(uv) + vec2(1., 0.);
    uv = mod(uv, 16.) / 16.;
    vec2 uv_c = uv - 0.5;

    float angle = atan(uv_c.y, uv_c.x + 0.00001) + PI;
    float radius = length(uv_c);
    float hue_b = (angle / TAU) + (Time * 5.) - (0.5 * radius);
    hue_b = fract(hue_b);

    float hue_i = uv.y * 0.75 + uv.x * 0.125 - (Time * 7.);
    hue_i = fract(hue_i);

    float hue = mix(hue_b, hue_i, step(0.5, texcolor.r));
    float saturation = texcolor.g;
    float value = texcolor.b;
    vec3 rgb = hsv2rgb(vec3(hue, saturation, value));

    rgb = mix(rgb, texcolor.rgb, step(0.5, texcolor.a));
    vec4 color = vec4(rgb, texcolor.a);

    if (color.a < 0.1) {
        discard;
    }
    color.a = 1.;
    color *= vertexColor * ColorModulator;
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    color *= lightMapColor;
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
