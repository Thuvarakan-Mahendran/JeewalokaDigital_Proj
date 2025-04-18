import React, { useState, useEffect } from 'react';
import { KeyRound, Pencil, Trash2 } from 'lucide-react';
import { getUsers, saveUser, editsUser, deleteUser } from "../../api/UserService";
import { saveUserCred, getUserCreds, deleteUserCred } from "../../api/UserCredService"
import { useWebSocket } from '../../Context/WebSocketContext';

const Users = () => {
    const [users, setUsers] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [isEditing, setIsEditing] = useState(false);
    const [editingUser, setEditingUser] = useState(null);
    const [isUserCred, setIsUserCred] = useState(false);
    const [userCred, setUserCred] = useState(null)
    const [userCreds, setUserCreds] = useState([]);
    // const [refresh, setRefresh] = useState(false);
    const [isViewMode, setIsViewMode] = useState(false)
    const { isConnected, onlineUsers } = useWebSocket();
    const [contactError, setContactError] = useState('');

    useEffect(() => {
        fetchUsers();
        fetchUserCreds();
    }, []);

    const fetchUsers = async () => {
        try {
            const response = await getUsers();
            setUsers(Array.isArray(response) ? response : response?.data || []);
        } catch (error) {
            console.error("Error fetching users:", error);
            setUsers([]);
        }
    };

    const fetchUserCreds = async () => {
        try {
            const roleResponse = await getUserCreds();
            console.log(roleResponse)
            setUserCreds(Array.isArray(roleResponse) ? roleResponse : roleResponse?.data || []);
        } catch (error) {
            console.error("Error fetching cred role:", error);
        }
    }

    const handleOnlineCheck = (username) => {
        const isOnline = isConnected && onlineUsers.has(username);
        return isConnected ? (isOnline ? 'bg-green-500' : 'bg-gray-400') : 'bg-orange-400';
    }

    const handleDelete = async (uid) => {
        if (window.confirm('Are you sure you want to delete this user?')) {
            try {
                await deleteUser(uid);
                setUsers((prevUsers) =>
                    prevUsers.filter((user) => user.uid !== uid)
                );
            } catch (error) {
                console.error("Error deleting user:", error);
            }
        }
    };

    const handleEdit = (user) => {
        setIsEditing(true);
        setEditingUser({ ...user });
    };

    const handleUpdate = async (e) => {
        e.preventDefault();
        if (!isValidEmail(editingUser.email)) {
            console.log("entered handle update")
            alert("Please enter a valid email")
            return;
        }
        try {
            await editsUser(editingUser);
            setUsers((prevUsers) =>
                prevUsers.map((user) =>
                    user.uid === editingUser.uid ? editingUser : user
                )
            );
            setIsEditing(false);
            setEditingUser(null);
        } catch (error) {
            console.error("Error updating user:", error);
        }
    };

    const handleSave = async (e) => {
        e.preventDefault()
        if (!isValidEmail(editingUser.email)) {
            console.log("entered handle save")
            alert("Please enter a valid email")
            return;
        }
        try {
            const savedUser = await saveUser(editingUser);
            setUsers((prevUsers) => [...prevUsers, savedUser]);
            setIsEditing(false);
            setEditingUser(null);
        } catch (error) {
            console.error("Error saving user:", error);
        }
    };

    const handleCredForm = (user, matchingCred) => {
        console.log("user id is: " + user.uid)
        matchingCred ? setIsViewMode(true) : setIsViewMode(false)
        setUserCred({
            userCredID: matchingCred ? matchingCred.userCredID : '',
            username: matchingCred ? matchingCred.username : '',
            password: '',
            role: matchingCred ? matchingCred.role : '',
            user: user.uid
        })
        setIsUserCred(true);
    }

    const handleCredSave = async () => {
        try {
            console.log("inside handle cred save")
            await saveUserCred(userCred)
            alert("Credentials created successfully")
            // setRefresh(prev => !prev)
            setIsUserCred(false);
            setUserCred({ username: '', password: '', role: '', user: '' });
        } catch (error) {
            console.error("Error saving credentials:", error);
            alert("failed to create credentials")
        }
    }

    const handleUserCredDeletion = async (userCredID) => {
        try {
            await deleteUserCred(userCredID)
            alert("Credentials deleted successfully")
            // setRefresh(prev => !prev)
        } catch (error) {
            console.error("Error deleting credentials:", error);
        }
    }

    const isValidEmail = (email) => {
        console.log("entered is valid email")
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
        return emailRegex.test(email)
    }

    const handleContactChange = (e) => {
        setEditingUser({ ...editingUser, contact: e.target.value })
        if (/^\d*$/.test(e.target.value)) {
            if (e.target.value.length == 10) {
                setContactError('')
            } else {
                setContactError('Please enter a valid 10 digit phone number')
            }
        } else {
            setContactError('only numbers are allowed')
        }
    }

    const filteredUsers = users.filter(user =>
        user.uname.toLowerCase().includes(searchTerm.toLowerCase())
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
                                uid: '',
                                uname: '',
                                // role: '',
                                contact: '',
                                email: '',
                                status: ''
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
                            {editingUser.uid ? 'Edit User' : 'Add New User'}
                        </h2>
                        <form onSubmit={editingUser.uid ? handleUpdate : handleSave}>
                            <div className="mb-4">
                                <label className="block mb-2">Name</label>
                                <input
                                    type="text"
                                    className="w-full px-3 py-2 border rounded"
                                    value={editingUser.uname}
                                    onChange={(e) => setEditingUser({ ...editingUser, uname: e.target.value })}
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block mb-2">Contact</label>
                                <input
                                    type="text"
                                    className="w-full px-3 py-2 border rounded"
                                    value={editingUser.contact}
                                    // onChange={(e) => setEditingUser({ ...editingUser, contact: e.target.value })}
                                    onChange={handleContactChange}
                                    maxLength={10}
                                />
                                {contactError && <p className='text-red-500 text-sm mt-1'>{contactError}</p>}
                            </div>
                            <div className="mb-4">
                                <label className="block mb-2">Email</label>
                                <input
                                    type="email"
                                    className="w-full px-3 py-2 border rounded"
                                    value={editingUser.email}
                                    onChange={(e) => setEditingUser({ ...editingUser, email: e.target.value })}
                                />
                            </div>
                            {/* <div className="mb-4">
                                <label className="block mb-2">Role</label>
                                <input
                                    type="text"
                                    className="w-full px-3 py-2 border rounded"
                                    value={editingUser.role}
                                    onChange={(e) => setEditingUser({ ...editingUser, role: e.target.value })}
                                />
                            </div> */}
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
                                    type="submit"
                                    className="px-4 py-2 bg-blue-600 text-white rounded"
                                // onClick={() => {
                                //     if (editingUser.uid) {
                                //         handleUpdate();
                                //     } else {
                                //         handleSave(editingUser);
                                //     }
                                // }}
                                >
                                    {editingUser.uid ? 'Save Changes' : 'Add User'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            {isUserCred && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white p-6 rounded-lg w-96">
                        <h2 className="text-xl font-bold mb-4">
                            Add User Credentials
                        </h2>
                        <form>
                            <div className="mb-4">
                                <label className="block mb-2">UserName</label>
                                <input
                                    type="text"
                                    className="w-full px-3 py-2 border rounded"
                                    readOnly={isViewMode}
                                    value={userCred.username}
                                    onChange={(e) => setUserCred({ ...userCred, username: e.target.value })}
                                />
                            </div>
                            {!isViewMode &&
                                <div className="mb-4">
                                    <label className="block mb-2">Password</label>
                                    <input
                                        type="password"
                                        className="w-full px-3 py-2 border rounded"
                                        value={userCred.password}
                                        onChange={(e) => setUserCred({ ...userCred, password: e.target.value })}
                                    />
                                </div>
                            }
                            <div className="mb-4">
                                <label className="block mb-2">Role</label>
                                <input
                                    type="text"
                                    className="w-full px-3 py-2 border rounded"
                                    readOnly={isViewMode}
                                    value={userCred.role}
                                    onChange={(e) => setUserCred({ ...userCred, role: e.target.value })}
                                />
                            </div>
                            <div className="flex justify-end gap-2">
                                <button
                                    type="button"
                                    className="px-4 py-2 border rounded"
                                    onClick={() => {
                                        setIsUserCred(false);
                                        setUserCred({ username: '', password: '', usermark: '' });
                                    }}
                                >
                                    Cancel
                                </button>
                                {isViewMode ?
                                    <button
                                        onClick={() => handleUserCredDeletion(userCred.userCredID)}
                                        className="px-4 py-2 bg-blue-600 text-white rounded"
                                    >
                                        Delete
                                    </button>
                                    :
                                    <button
                                        onClick={handleCredSave}
                                        className="px-4 py-2 bg-blue-600 text-white rounded"
                                    >
                                        Add UserCred
                                    </button>
                                }
                            </div>
                        </form>
                    </div>
                </div >
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
                        {filteredUsers.map((user) => {
                            const matchingCred = userCreds != null ? userCreds.find((usercred) => usercred.user === user.uid) : null
                            const statusClass = handleOnlineCheck(user.uname);
                            console.log(user.uname, " ", statusClass)
                            return (
                                <tr key={user.uname} className="border-b hover:bg-gray-50">
                                    <td className="px-6 py-4">{user.uid}</td>
                                    <td className="px-6 py-4">{user.uname}</td>
                                    <td className="px-6 py-4">{matchingCred ? matchingCred.role : ''}</td>
                                    <td className="px-6 py-4">{user.contact}</td>
                                    <td className="px-6 py-4">{user.email}</td>
                                    <td className="px-6 py-4">
                                        <div className="flex items-center">
                                            <span className={`inline-block w-3 h-3 mr-2 rounded-full ${statusClass}`}></span>
                                            {/* <span className="inline-block w-3 h-3 mr-2 rounded-full text-red-500"></span> */}
                                        </div>
                                    </td>
                                    <td className="px-6 py-4">
                                        <div className="flex gap-2">
                                            <button
                                                onClick={() => handleEdit(user)}
                                                className="p-1 hover:bg-gray-100 rounded"
                                            >
                                                <Pencil className="w-5 h-5 text-blue-600" />
                                            </button>
                                            <button
                                                onClick={() => handleCredForm(user, matchingCred)}
                                                className="p-1 hover:bg-gray-100 rounded"
                                            >
                                                <KeyRound className='w-5 h-5 text-green-600' />
                                            </button>
                                            <button
                                                onClick={() => handleDelete(user.uid)}
                                                className="p-1 hover:bg-gray-100 rounded"
                                            >
                                                <Trash2 className="w-5 h-5 text-red-600" />
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            )
                        }
                        )}
                    </tbody>
                </table>
            </div>
        </div >
    );
};

export default Users;