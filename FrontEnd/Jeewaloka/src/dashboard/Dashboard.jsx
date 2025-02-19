import { useState } from "react";

export default function Dashboard() {
  return (
    <div className="flex items-start justify-end min-h-screen bg-gray-100 p-10">
      <div className="p-6 bg-white rounded-xl shadow-md text-center">
        <div className="flex flex-col items-center">
          <img
            className="w-12 h-12 rounded-full"
            src="https://flowbite.com/docs/images/people/profile-picture-5.jpg"
            alt="user photo"
          />
          <p className="mt-2 text-sm font-semibold">Otor John</p>
          <p className="text-xs text-gray-500">ojohn@example.com</p>
        </div>
        <div className="mt-4 space-y-2">
          <a href="#" className="block px-3 py-2 rounded-lg text-sm font-medium hover:bg-gray-100">
            Dashboard
          </a>
          <a href="#" className="block px-3 py-2 rounded-lg text-sm font-medium hover:bg-gray-100">
            Settings
          </a>
          <a href="#" className="block px-3 py-2 rounded-lg text-sm font-medium text-red-500 hover:bg-gray-100">
            Signout
          </a>
        </div>
      </div>
    </div>
  );
}
