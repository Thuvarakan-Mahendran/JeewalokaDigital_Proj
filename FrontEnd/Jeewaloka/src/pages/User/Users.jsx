import { useEffect, useState } from "react";
import {
    getUsers,
    saveUser,
    editUser,
    deleteUser
} from "../../api/UserService";

const Users = () => {
    const [users, setUsers] = useState([])
    const [search, setSearch] = useState("")
    const [showPopup, setShowPopup] = useState(false)
    const [userForm, setUserForm] = useState({
        userId: "",
        userName: "",
        userContact: "",
        userEmail: "",
        userRole: "",
        userLastLogin: ""
    })
    const [editingUser, setEditingUser] = useState(null)

    useEffect(() => {
        fetchUsers()
    }, [])

    const fetchUsers = async () => {
        try {
            const response = await getUsers()
            setUsers(Array.isArray(response) ? response : response?.data || [])
        } catch (error) {
            console.error("Error fetching suppliers:", error)
            setUsers([])
        }
    }

    const handleDeleteUser = async (userId) => {
        try {
            await deleteUser(userId)
            setUsers((prevUsers) =>
                prevUsers.filter((s) => s.userId !== userId)
            );
        } catch (error) {
            console.error("Error deleting user:", error)
        }
    }

    const handleInputChange = (e) => {
        setUserForm((prevForm) => ({
            ...prevForm,
            [e.target.name]: e.target.value,
        }))
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        try {
            if (editingUser) {
                await editUser({ ...editingUser, ...userForm })
            } else {
                await saveUser(userForm)
            }
            setUserForm({
                userName: "",
                userContact: "",
                userEmail: "",
                supplierStatus: ""
            });
            setEditingUser(null)
            setShowPopup(false)
            fetchUsers()
        } catch (error) {
            console.error("Error saving user:", error)
        }
    }

    const handleEdit = (user) => {
        setEditingUser(user)
        setUserForm(user)
        setShowPopup(true)
    }

    const filteredUsers = users.filter((user) =>
        user.userName.toLowerCase().includes(search.toLowerCase())
    )

    return (
        <div className="p-6 bg-gray-50 min-h-screen">
            <h2 className="text-2xl font-semibold text-gray-800">All Users</h2>

            {/* Search */}
            <div className="bg-white p-4 shadow rounded-lg flex justify-between mb-4 items-center">
                <input
                    type="text"
                    placeholder="Search..."
                    className="w-1/3 rounded border-[1.5px] border-stroke bg-gray-100 py-2 px-5"
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />
                <button
                    className="bg-blue-600 text-white px-4 py-2 rounded-lg"
                    onClick={() => {
                        setEditingUser(null); // Ensure we are not in edit mode
                        setUserForm({
                            userId: "",
                            userName: "",
                            userContact: "",
                            userEmail: "",
                            userStatus: "",
                            userCreatedDate: ""
                        }); // Reset form
                        setShowPopup(true);
                    }}
                >
                    Add new user
                </button>
            </div>

            {/* Table */}
            <div className="bg-white p-4 shadow rounded-lg">
                <table className="w-full text-left border-collapse">
                    <thead>
                        <tr className="bg-gray-100">
                            <th className="p-3">UID</th>
                            <th className="p-3">UName</th>
                            <th className="p-3">UContact</th>
                            <th className="p-3">UEmail</th>
                            <th className="p-3">UStatus</th>
                            <th className="p-3">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {filteredUsers.map((user) => (
                            <tr
                                key={user.userId}
                                className="border-b hover:bg-gray-50"
                            >
                                <td className="p-3">{user.userId}</td>
                                <td className="p-3">{user.userName}</td>
                                <td className="p-3">{user.userContact}</td>
                                <td className="p-3">{user.userEmail}</td>
                                <td className="p-3">{user.userStatus}</td>
                                <td className="p-3 flex space-x-4">
                                    <button
                                        className="text-blue-600"
                                        onClick={() => handleEdit(user)}
                                    >
                                        Edit
                                    </button>
                                    <button
                                        className="text-red-600"
                                        onClick={() => handleDeleteSupplier(user.userId)}
                                    >
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default Users
