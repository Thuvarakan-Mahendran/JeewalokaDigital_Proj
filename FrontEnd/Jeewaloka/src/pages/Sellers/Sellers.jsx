
const sellers = [
    { id: 1, name: "", phone: "", email: "", address: "" },
    { id: 2, name: "", phone: "", email: "", address: "" },
    { id: 3, name: "", phone: "", email: "", address: "" },
    { id: 4, name: "", phone: "", email: "", address: "" },
  ];

const Sellers = () => {


    return(
        <div className="p-10">
            <h1 className="text-3xl font-bold p-2">Sellers</h1>
            <input type= "text" className="border border-gray-400 px-3 py-2 rounded w-64 focus:outline-none" id="sellerid" name="sellerid" placeholder="Search By Name or SellerId"></input>
            <br/><br/><br/>
            <input type = "Submit" value = "Create Seller" className = "px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"></input>
            <div className="overflow-x-auto p-6">
      <table className="w-full border-collapse border border-gray-300">
        <thead>
          <tr className="bg-gray-100">
            <th className="border border-gray-300 px-4 py-2 text-left">SellerId</th>
            <th className="border border-gray-300 px-4 py-2 text-left">NAME</th>
            <th className="border border-gray-300 px-4 py-2 text-left">TelephoneNo</th>
            <th className="border border-gray-300 px-4 py-2 text-left">Email</th>
            <th className="border border-gray-300 px-4 py-2 text-left">Address</th>
            <th className="border border-gray-300 px-4 py-2 text-left">ACTIONS</th>
          </tr>
        </thead>
        <tbody>
          {sellers.map((seller) => (
            <tr key={seller.id} className="border border-gray-300">
              <td className="border border-gray-300 px-4 py-2">{seller.id}</td>
              <td className="border border-gray-300 px-4 py-2">{seller.name}</td>
              <td className="border border-gray-300 px-4 py-2">{seller.phone}</td>
              <td className="border border-gray-300 px-4 py-2">{seller.email}</td>
              <td className="border border-gray-300 px-4 py-2">{seller.address}</td>
              <td className="border border-gray-300 px-4 py-2">
                <button className="text-blue-600 mr-2">Edit</button>
                <button className="text-red-600">Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
        </div>




    );

};

export default Sellers;