# 📸 Lab13 – Implementación de CameraX en Android Studio

Este proyecto corresponde al **Laboratorio 13** del curso de **Programación en Móviles**, en el cual se implementa una aplicación Android utilizando **CameraX** para capturar fotografías, alternar entre cámaras y visualizar una galería de imágenes tomadas por el usuario.

## 🎯 Objetivo del laboratorio

Desarrollar una aplicación funcional en Android que permita:

- Capturar fotos usando la cámara del dispositivo.
- Alternar entre cámara frontal y trasera.
- Almacenar las imágenes capturadas localmente.
- Visualizar las fotos mediante un `ViewPager2` en una galería integrada.

## 🧰 Tecnologías utilizadas

- **Lenguaje:** Kotlin
- **IDE:** Android Studio
- **Versión mínima del SDK:** 24 (Nougat)
- **API de cámara:** CameraX
- **UI y componentes:** ViewBinding, ConstraintLayout, RecyclerView, ViewPager2
- **Librerías adicionales:**
  - Glide (`com.github.bumptech.glide:glide:4.12.0`)

## 🗂️ Estructura del proyecto

```text
Lab13/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/milla/antonella/lab13/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── GalleryActivity.kt
│   │   │   │   ├── GalleryAdapter.kt
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_main.xml
│   │   │   │   │   ├── activity_gallery.xml
│   │   │   │   │   ├── list_item_img.xml
│   │   │   ├── AndroidManifest.xml


