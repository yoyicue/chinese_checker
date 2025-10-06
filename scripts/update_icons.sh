#!/usr/bin/env bash
set -euo pipefail

# Regenerate Android launcher icons from a single square PNG source.
# Usage: scripts/update_icons.sh [path/to/base.png]

ROOT_DIR=$(cd "$(dirname "$0")/.." && pwd)
RES_DIR="$ROOT_DIR/app/src/main/res"
TMP_DIR="$ROOT_DIR/.icons-tmp"

SRC=""

command -v sips >/dev/null 2>&1 || { echo "Error: 'sips' not found (macOS)." >&2; exit 1; }

# Defaults
FG_SHRINK=1.0          # 1.0 = 不缩放；<1.0 = 缩小增加留白
LEGACY_SHRINK=1.0
# 读取背景色（用于前景 padding 的填充色），失败则退回 1B1B1B
BG_HEX_DEFAULT=$(sed -n 's/.*<color name="ic_launcher_background">#\([0-9A-Fa-f]\{6\}\)<\/color>.*/\1/p' "$RES_DIR/values/ic_launcher_background.xml" | head -1 || true)
PAD_HEX=${BG_HEX_DEFAULT:-1B1B1B}

# Parse options and optional SRC path
for arg in "$@"; do
  case "$arg" in
    --shrink=*)
      v="${arg#*=}"; FG_SHRINK="$v"; LEGACY_SHRINK="$v" ;;
    --fg-shrink=*)
      FG_SHRINK="${arg#*=}" ;;
    --legacy-shrink=*)
      LEGACY_SHRINK="${arg#*=}" ;;
    --pad=*)
      PAD_HEX="${arg#*=}" ;;
    --*) ;;
    *) SRC="$arg" ;;
  esac
done

mkdir -p "$TMP_DIR"
trap 'rm -rf "$TMP_DIR"' EXIT

# Resolve SRC if not provided
if [[ -z "${SRC}" ]]; then
  if [[ -f "$ROOT_DIR/icon1024.png" ]]; then
    SRC="$ROOT_DIR/icon1024.png"
  elif [[ -f "$ROOT_DIR/icon1024x1024.png" ]]; then
    SRC="$ROOT_DIR/icon1024x1024.png"
  elif [[ -f "$ROOT_DIR/icon512x512.png" ]]; then
    SRC="$ROOT_DIR/icon512x512.png"
  else
    echo "Error: Please provide a base PNG or place icon1024.png at repo root." >&2
    exit 1
  fi
fi

if [[ ! -f "$SRC" ]]; then
  echo "Error: Source image not found: $SRC" >&2
  exit 1
fi

echo "Using base image: $SRC"

# Expected output sizes (px) as dir:size pairs (avoid associative arrays for macOS bash)
LEGACY=(
  "mipmap-mdpi:48"
  "mipmap-hdpi:72"
  "mipmap-xhdpi:96"
  "mipmap-xxhdpi:144"
  "mipmap-xxxhdpi:192"
)

FG=(
  "mipmap-mdpi:108"
  "mipmap-hdpi:162"
  "mipmap-xhdpi:216"
  "mipmap-xxhdpi:324"
  "mipmap-xxxhdpi:432"
)

function calc_inner() {
  # args: size scale -> inner(int)
  awk -v s="$1" -v r="$2" 'BEGIN { printf("%d", (s*r)+0.5) }'
}

function gen_png_padded() {
  # args: size out scale pad_hex
  local size=$1 out=$2 scale=$3 pad=$4
  local dir tmp
  dir=$(dirname "$out")
  mkdir -p "$dir"
  tmp="$TMP_DIR/rsz_${size}.png"
  local inner
  inner=$(calc_inner "$size" "$scale")
  # 先缩到 inner，再 pad 到 size（用指定颜色）
  sips -s format png -z "$inner" "$inner" "$SRC" --out "$tmp" >/dev/null
  sips -s format png -p "$size" "$size" --padColor "$pad" "$tmp" --out "$out" >/dev/null
}

echo "Generating legacy ic_launcher icons (shrink=$LEGACY_SHRINK)..."
for pair in "${LEGACY[@]}"; do
  IFS=":" read -r d size <<< "$pair"
  out="$RES_DIR/$d/ic_launcher.png"
  if [[ -f "$out" ]]; then
    gen_png_padded "$size" "$out" "$LEGACY_SHRINK" "$PAD_HEX"
  fi
done

echo "Generating adaptive foreground icons (shrink=$FG_SHRINK, pad=$PAD_HEX)..."
for pair in "${FG[@]}"; do
  IFS=":" read -r d size <<< "$pair"
  out="$RES_DIR/$d/ic_launcher_foreground.png"
  if [[ -f "$out" ]]; then
    gen_png_padded "$size" "$out" "$FG_SHRINK" "$PAD_HEX"
  fi
done

echo "Done. Updated ic_launcher and ic_launcher_foreground PNGs."
