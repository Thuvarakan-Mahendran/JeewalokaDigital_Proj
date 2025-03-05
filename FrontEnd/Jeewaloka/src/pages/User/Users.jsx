import React, { useState, useEffect } from 'react';
import { Pencil, Trash2 } from 'lucide-react';
import { getUsers, saveUser, editsUser, deleteUser } from "../../api/UserService";

const Users = () => {
    const [users, setUsers] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [isEditing, setIsEditing] = useState(false);
    const [editingUser, setEditingUser] = useState(null);

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            const data = await getUsers();
            setUsers(data || []);
        } catch (error) {
            console.error("Error fetching users:", error);
            setUsers([]);
        }
    };

    const handleDelete = async (uid) => {
        if (window.confirm('Are you sure you want to delete this user?')) {
            try {
                await deleteUser(uid);
                setUsers((prevUsers) =>
                    prevUsers.filter((user) => user.UID !== uid)
                );
            } catch (error) {
                console.error("Error deleting user:", error);
            }
        }
    };

    const handleEdit = (user) => {
        setIsEditing(true);
        setEditingUser(user);
    };

    const handleUpdate = async (e) => {
        e.preventDefault();
        try {
            await editsUser(editingUser);
            setUsers((prevUsers) =>
                prevUsers.map((user) =>
                    user.UID === editingUser.UID ? editingUser : user
                )
            );
            setIsEditing(false);
            setEditingUser(null);
        } catch (error) {
            console.error("Error updating user:", error);
        }
    };

    const handleSave = async (newUser) => {
        try {
            const savedUser = await saveUser(newUser);
            setUsers((prevUsers) => [...prevUsers, savedUser]);
            setIsEditing(false);
            setEditingUser(null);
        } catch (error) {
            console.error("Error saving user:", error);
        }
    };

    const filteredUsers = users.filter(user =>
        user.UName.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">All Users</h1>
                <div className="flex gap-4">
                    <input
                        type="text"
                        placeholder="Search..."
                        className="px-4 py-2 border rounded-lg"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                    <button
                        className="bg-blue-600 text-white px-4 py-2 rounded-lg"
                        onClick={() => {
                            setIsEditing(true);
                            setEditingUser({
                                UName: '',
                                UContact: '',
                                UEmail: '',
                                UStatus: '',
                                URole: ''
                            });
                        }}
                    >
                        Add new user
                    </button>
                </div>
            </div>

            {isEditing && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white p-6 rounded-lg w-96">
                        <h2 className="text-xl font-bold mb-4">
                            {editingUser.UID ? 'Edit User' : 'Add New User'}
                        </h2>
                        <form onSubmit={handleSave}>
                            <div className="mb-4">
                                <label className="block mb-2">Name</label>
                                <input
                                    type="text"
                                    className="w-full px-3 py-2 border rounded"
                                    value={editingUser.UName}
                                    onChange={(e) => setEditingUser({ ...editingUser, UName: e.target.value })}
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block mb-2">Contact</label>
                                <input
                                    type="text"
                                    className="w-full px-3 py-2 border rounded"
                                    value={editingUser.UContact}
                                    onChange={(e) => setEditingUser({ ...editingUser, UContact: e.target.value })}
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block mb-2">Email</label>
                                <input
                                    type="email"
                                    className="w-full px-3 py-2 border rounded"
                                    value={editingUser.UEmail}
                                    onChange={(e) => setEditingUser({ ...editingUser, UEmail: e.target.value })}
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block mb-2">Role</label>
                                <input
                                    type="text"
                                    className="w-full px-3 py-2 border rounded"
                                    value={editingUser.URole}
                                    onChange={(e) => setEditingUser({ ...editingUser, URole: e.target.value })}
                                />
                            </div>
                            <div className="flex justify-end gap-2">
                                <button
                                    type="button"
                                    className="px-4 py-2 border rounded"
                                    onClick={() => {
                                        setIsEditing(false);
                                        setEditingUser(null);
                                    }}
                                >
                                    Cancel
                                </button>
                                <button
                                    type="button"
                                    className="px-4 py-2 bg-blue-600 text-white rounded"
                                    onClick={() => {
                                        if (editingUser.UID) {
                                            handleUpdate();
                                        } else {
                                            handleSave(editingUser);
                                        }
                                    }}
                                >
                                    {editingUser.UID ? 'Save Changes' : 'Add User'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            <div className="bg-white rounded-lg shadow">
                <table className="w-full">
                    <thead>
                        <tr className="border-b">
                            <th className="px-6 py-3 text-left">UID</th>
                            <th className="px-6 py-3 text-left">Name</th>
                            <th className="px-6 py-3 text-left">Role</th>
                            <th className="px-6 py-3 text-left">Contact</th>
                            <th className="px-6 py-3 text-left">Email</th>
                            <th className="px-6 py-3 text-left">Status</th>
                            <th className="px-6 py-3 text-left">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {filteredUsers.map((user) => (
                            <tr key={user.UID} className="border-b hover:bg-gray-50">
                                <td className="px-6 py-4">{user.UID}</td>
                                <td className="px-6 py-4">{user.UName}</td>
                                <td className="px-6 py-4">{user.UContact}</td>
                                <td className="px-6 py-4">{user.UEmail}</td>
                                <td className="px-6 py-4">{user.UStatus}</td>
                                <td className="px-6 py-4">
                                    <div className="flex gap-2">
                                        <button
                                            onClick={() => handleEdit(user)}
                                            className="p-1 hover:bg-gray-100 rounded"
                                        >
                                            <Pencil className="w-5 h-5 text-blue-600" />
                                        </button>
                                        <button
                                            onClick={() => handleDelete(user.UID)}
                                            className="p-1 hover:bg-gray-100 rounded"
                                        >
                                            <Trash2 className="w-5 h-5 text-red-600" />
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default Users;