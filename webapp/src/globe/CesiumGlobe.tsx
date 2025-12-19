import { useEffect, useRef } from 'react';
import {
  Viewer,
  Ion,
  Cartesian3,
  Color,
  Entity,
  createWorldTerrainAsync,
} from 'cesium';
import 'cesium/Build/Cesium/Widgets/widgets.css';
import type { Flight } from '../types/flight';

Ion.defaultAccessToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiI5NDVkNGNhYy05NmE2LTQwMTEtYTBiMC1jZGFlZGNmMmUwNWEiLCJpZCI6MjYwODU3LCJpYXQiOjE3MzQ1NzI3ODR9.d9ZQKEznaseljjLZmDKD8LQV7rIFuPfz9R5OShaPSWw';

interface CesiumGlobeProps {
  flights: Flight[];
}

export const CesiumGlobe: React.FC<CesiumGlobeProps> = ({ flights }) => {
  const viewerRef = useRef<Viewer | null>(null);
  const containerRef = useRef<HTMLDivElement>(null);
  const entitiesRef = useRef<Map<string, Entity>>(new Map());

  useEffect(() => {
    if (!containerRef.current || viewerRef.current) return;

    const initViewer = async () => {
      if (!containerRef.current) return;

      const viewer = new Viewer(containerRef.current, {
        animation: false,
        timeline: false,
        baseLayerPicker: false,
        geocoder: false,
        homeButton: false,
        sceneModePicker: false,
        navigationHelpButton: false,
        fullscreenButton: true,
        vrButton: false,
        shouldAnimate: true,
        infoBox: true,
        selectionIndicator: true,
      });

      try {
        viewer.terrainProvider = await createWorldTerrainAsync();
      } catch (error) {
        console.warn('Failed to load terrain, using default ellipsoid:', error);
      }

      viewer.scene.globe.enableLighting = true;
      viewer.scene.globe.atmosphereLightIntensity = 10.0;

      if (viewer.scene.skyAtmosphere) {
        viewer.scene.skyAtmosphere.show = true;
      }

      viewer.camera.setView({
        destination: Cartesian3.fromDegrees(0, 20, 20000000),
      });

      viewerRef.current = viewer;
    };

    initViewer();

    return () => {
      if (viewerRef.current) {
        viewerRef.current.destroy();
        viewerRef.current = null;
      }
    };
  }, []);

  useEffect(() => {
    if (!viewerRef.current) return;

    const viewer = viewerRef.current;
    const currentFlightIds = new Set(flights.map((f) => f.flightId));
    const existingFlightIds = new Set(entitiesRef.current.keys());

    existingFlightIds.forEach((id) => {
      if (!currentFlightIds.has(id)) {
        const entity = entitiesRef.current.get(id);
        if (entity) {
          viewer.entities.remove(entity);
          entitiesRef.current.delete(id);
        }
      }
    });

    flights.forEach((flight) => {
      if (!flight.latitude || !flight.longitude) return;

      let entity = entitiesRef.current.get(flight.flightId);

      if (!entity) {
        entity = viewer.entities.add({
          id: flight.flightId,
          position: Cartesian3.fromDegrees(
            flight.longitude,
            flight.latitude,
            flight.altitude
          ),
          point: {
            pixelSize: 8,
            color: Color.CYAN,
            outlineColor: Color.WHITE,
            outlineWidth: 2,
          },
          label: {
            text: flight.flightNumber || flight.callsign,
            font: '12px sans-serif',
            fillColor: Color.WHITE,
            outlineColor: Color.BLACK,
            outlineWidth: 2,
            style: 0,
            pixelOffset: new Cartesian3(0, -15, 0),
            showBackground: true,
            backgroundColor: Color.fromAlpha(Color.BLACK, 0.7),
          },
          description: `
            <div style="font-family: sans-serif;">
              <h3>${flight.flightNumber || flight.callsign}</h3>
              <p><strong>From:</strong> ${flight.departureAirportName || flight.departureAirportIcao}</p>
              <p><strong>To:</strong> ${flight.destinationAirportName || flight.destinationAirportIcao}</p>
              <p><strong>Altitude:</strong> ${Math.round(flight.altitude)} m</p>
              <p><strong>Speed:</strong> ${Math.round(flight.speed)} km/h</p>
              <p><strong>Status:</strong> ${flight.status}</p>
            </div>
          `,
        });
        entitiesRef.current.set(flight.flightId, entity);
      } else {
        entity.position = Cartesian3.fromDegrees(
          flight.longitude,
          flight.latitude,
          flight.altitude
        ) as any;
        if (entity.label) {
          entity.label.text = flight.flightNumber || flight.callsign as any;
        }
        entity.description = `
          <div style="font-family: sans-serif;">
            <h3>${flight.flightNumber || flight.callsign}</h3>
            <p><strong>From:</strong> ${flight.departureAirportName || flight.departureAirportIcao}</p>
            <p><strong>To:</strong> ${flight.destinationAirportName || flight.destinationAirportIcao}</p>
            <p><strong>Altitude:</strong> ${Math.round(flight.altitude)} m</p>
            <p><strong>Speed:</strong> ${Math.round(flight.speed)} km/h</p>
            <p><strong>Status:</strong> ${flight.status}</p>
          </div>
        ` as any;
      }
    });
  }, [flights]);

  return (
    <div
      ref={containerRef}
      style={{
        width: '100%',
        height: '100%',
        position: 'absolute',
        top: 0,
        left: 0,
      }}
    />
  );
};
