import { useAuth } from '../hooks/useAuth';

export default function Settings() {
  const { username, displayName, role } = useAuth();

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold text-white mb-6">Settings</h1>

      <div className="bg-gray-800 rounded-lg p-6 border border-gray-700 max-w-lg">
        <h2 className="text-lg font-semibold text-white mb-4">Profile</h2>
        <div className="space-y-3">
          <div>
            <span className="text-gray-400 text-sm">Username</span>
            <p className="text-white">{username}</p>
          </div>
          <div>
            <span className="text-gray-400 text-sm">Display Name</span>
            <p className="text-white">{displayName}</p>
          </div>
          <div>
            <span className="text-gray-400 text-sm">Role</span>
            <span className={`inline-block px-2 py-1 rounded text-xs font-medium mt-1 ${
              role === 'ADMIN' ? 'bg-red-500/20 text-red-400' :
              role === 'ANALYST' ? 'bg-green-500/20 text-green-400' :
              'bg-blue-500/20 text-blue-400'
            }`}>
              {role}
            </span>
          </div>
        </div>
      </div>
    </div>
  );
}
