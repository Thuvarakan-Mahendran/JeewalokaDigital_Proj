import { useState } from "react";

export default function DashboardPage() {
  const [user] = useState({ name: "Neil Sims", email: "neil.sims@flowbite.com" });

  return (
    <div className="flex justify-center items-center min-h-screen bg-gray-100">
      <div className="grid grid-cols-3 gap-4">
        <Card className="h-40" />
        <Card className="h-40" />
        <UserProfile user={user} />
      </div>
    </div>
  );
}

function Card({ className, children }) {
  return (
    <div className={`bg-white shadow-md rounded-lg p-4 ${className}`}>{children}</div>
  );
}

function UserProfile({ user }) {
  return (
    <Card className="p-4 flex flex-col items-center text-center">
      <img
        className="w-12 h-12 rounded-full"
        src="https://flowbite.com/docs/images/people/profile-picture-5.jpg"
        alt="user photo"
      />
      <div className="font-semibold text-lg">{user.name}</div>
      <div className="text-sm text-gray-500">{user.email}</div>
    </Card>
  );
}
