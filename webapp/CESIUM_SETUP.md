# Cesium Globe Setup

## What's Configured

✅ **Cesium Ion Access Token** - Valid token for loading Cesium assets
✅ **World Terrain** - 3D terrain data from Cesium Ion
✅ **Day/Night Lighting** - Realistic sun-based lighting
✅ **Sky Atmosphere** - Earth's atmosphere rendering
✅ **Full-screen Container** - Globe fills entire viewport

## Testing the Globe

Run the app and you should see:

```bash
cd webapp
npm run dev
```

Open http://localhost:3000

You should see:
- 3D Earth globe rotating
- Day/night terminator line (sunlight/shadow)
- Sky atmosphere (blue glow around Earth)
- Stars in the background
- Navigation controls (mouse/touch)

## Cesium Controls

- **Left-click + drag** - Rotate the globe
- **Right-click + drag** - Pan the camera
- **Scroll wheel** - Zoom in/out
- **Middle-click + drag** - Tilt view

## If Globe Doesn't Render

1. Check browser console for errors
2. Verify internet connection (Cesium loads assets from CDN)
3. Check that the container has size:
   - Inspect element on the globe div
   - Should have `width: 100%` and `height: 100%`
4. Try refreshing the page

## Current State

- Globe renders even without backend connection
- Flight data will show error banner until backend is running
- Once backend is ready at `http://localhost:8080/api/flights`, airplanes will appear
