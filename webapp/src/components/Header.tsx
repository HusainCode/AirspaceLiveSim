import './Header.css';

interface HeaderProps {
  lastUpdate?: Date;
}

export const Header: React.FC<HeaderProps> = ({ lastUpdate }) => {
  return (
    <header className="app-header">
      <div className="header-content">
        <h1 className="app-title">AirSpace Live Sim</h1>
        {lastUpdate && (
          <div className="last-update">
            Last update: {lastUpdate.toLocaleTimeString()}
          </div>
        )}
      </div>
    </header>
  );
};
