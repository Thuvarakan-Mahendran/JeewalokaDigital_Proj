import { useState } from "react";
import { Link, Outlet } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faBars,
  faUser,
  faChartBar,
  faTachometerAlt,
  faWarehouse,
  faDollarSign,
  faChevronDown,
} from "@fortawesome/free-solid-svg-icons";

const Dashboard = () => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [isInventoryDropdownOpen, setIsInventoryDropdownOpen] = useState(false);
  const [isSalesDropdownOpen, setIsSalesDropdownOpen] = useState(false);

  return (
    <>
      <nav className="fixed top-0 z-50 w-full bg-white border-b border-gray-200 dark:bg-gray-800 dark:border-gray-700">
        <div className="px-3 py-3 lg:px-5 lg:pl-3">
          <div className="flex items-center justify-between">
            <div className="flex items-center justify-start rtl:justify-end">
              {/* Sidebar Toggle Button */}
              <button
                onClick={() => setIsSidebarOpen(!isSidebarOpen)}
                type="button"
                className="inline-flex items-center p-2 text-sm text-gray-500 rounded-lg sm:hidden hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-gray-200 dark:text-gray-400 dark:hover:bg-gray-700 dark:focus:ring-gray-600"
              >
                <span className="sr-only">Open sidebar</span>
                <FontAwesomeIcon icon={faBars} className="w-6 h-6" />
              </button>
              <a href="https://flowbite.com" className="flex ms-2 md:me-24">
                <span className="self-center text-xl font-semibold sm:text-2xl whitespace-nowrap dark:text-white">
                  Jeewaloka Distribution System
                </span>
              </a>
            </div>
            <div className="flex items-center">
              {/* User Menu */}
              <div className="relative">
                <button
                  onClick={() => setIsDropdownOpen(!isDropdownOpen)}
                  type="button"
                  className="flex text-sm bg-gray-800 rounded-full focus:ring-4 focus:ring-gray-300 dark:focus:ring-gray-600"
                >
                  <span className="sr-only">Open user menu</span>
                  <img
                    className="w-8 h-8 rounded-full"
                    src="https://flowbite.com/docs/images/people/profile-picture-5.jpg"
                    alt="user photo"
                  />
                </button>

                {/* Dropdown Menu */}
                {isDropdownOpen && (
                  <div className="absolute right-0 mt-2 w-48 bg-white divide-y divide-gray-100 rounded shadow dark:bg-gray-700 dark:divide-gray-600">
                    <div className="px-4 py-3">
                      <p className="text-sm text-gray-900 dark:text-white">
                        Neil Sims
                      </p>
                      <p className="text-sm font-medium text-gray-900 truncate dark:text-gray-300">
                        neil.sims@flowbite.com
                      </p>
                    </div>
                    <ul className="py-1">
                      <li>
                        <Link
                          to="#"
                          className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-600 dark:hover:text-white"
                        >
                          Dashboard
                        </Link>
                      </li>
                      <li>
                        <Link
                          to="#"
                          className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-600 dark:hover:text-white"
                        >
                          Settings
                        </Link>
                      </li>
                      <li>
                        <Link
                          to="#"
                          className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-600 dark:hover:text-white"
                        >
                          Sign out
                        </Link>
                      </li>
                    </ul>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      </nav>

      <aside
        id="logo-sidebar"
        className={`fixed top-0 left-0 z-40 w-64 h-screen pt-20 transition-transform ${
          isSidebarOpen ? "translate-x-0" : "-translate-x-full"
        } bg-white border-r border-gray-200 sm:translate-x-0 dark:bg-gray-800 dark:border-gray-700`}
        aria-label="Sidebar"
      >
        <div className="h-full px-3 pb-4 overflow-y-auto bg-white dark:bg-gray-800">
          <ul className="space-y-2 font-semibold">
            <li>
              <Link
                to="/"
                className="flex items-center p-2 text-gray-900 dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group"
              >
                <FontAwesomeIcon
                  icon={faTachometerAlt}
                  className="w-5 h-5 text-gray-500 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white"
                />
                <span className="ms-3">Dashboard</span>
              </Link>
            </li>
            <li>
              <Link
                to="/sellers"
                className="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group"
              >
                <FontAwesomeIcon
                  icon={faUser}
                  className="w-5 h-5 text-gray-500 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white"
                />
                <span className="flex-1 ms-3 whitespace-nowrap">
                  User Management
                </span>
              </Link>
            </li>
            <li>
              <button
                onClick={() =>
                  setIsInventoryDropdownOpen(!isInventoryDropdownOpen)
                }
                className="flex items-center justify-between w-full p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700"
              >
                <div className="flex items-center gap-3">
                  <FontAwesomeIcon
                    icon={faWarehouse}
                    className="w-5 h-5 text-gray-500 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white"
                  />
                  <span className="whitespace-nowrap">Inventory</span>
                </div>

                <FontAwesomeIcon
                  icon={faChevronDown}
                  className={`w-3 h-3 transition-transform ${
                    isInventoryDropdownOpen ? "rotate-180" : ""
                  }`}
                />
              </button>

              {isInventoryDropdownOpen && (
                <ul className="ml-6 space-y-2">
                  <li>
                    <Link
                      to="inventary/items"
                      className="block px-4 py-2 text-gray-900 rounded-lg hover:bg-gray-100 dark:text-white dark:hover:bg-gray-700"
                    >
                      Items
                    </Link>
                  </li>
                  <li>
                    <Link
                      to="inventary/supplier"
                      className="block px-4 py-2 text-gray-900 rounded-lg hover:bg-gray-100 dark:text-white dark:hover:bg-gray-700"
                    >
                      Suppliers
                    </Link>
                  </li>
                  <li>
                    <Link
                      to="inventary/purchaseorder"
                      className="block px-4 py-2 text-gray-900 rounded-lg hover:bg-gray-100 dark:text-white dark:hover:bg-gray-700"
                    >
                      Purchase Order
                    </Link>
                  </li>
                  <li>
                    <Link
                      to="inventary/grn"
                      className="block px-4 py-2 text-gray-900 rounded-lg hover:bg-gray-100 dark:text-white dark:hover:bg-gray-700"
                    >
                      Goods Received Note
                    </Link>
                  </li>
                  <li>
                    <Link
                      to="/goods-returns-note"
                      className="block px-4 py-2 text-gray-900 rounded-lg hover:bg-gray-100 dark:text-white dark:hover:bg-gray-700"
                    >
                      Goods Returns Note
                    </Link>
                  </li>
                </ul>
              )}
            </li>

            <li>
              <button
                onClick={() => setIsSalesDropdownOpen(!isSalesDropdownOpen)}
                className="flex items-center justify-between w-full p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700"
              >
                <div className="flex items-center gap-3">
                  <FontAwesomeIcon
                    icon={faDollarSign}
                    className="w-5 h-5 text-gray-500 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white"
                  />
                  <span className="whitespace-nowrap">Sales</span>
                </div>
                <FontAwesomeIcon
                  icon={faChevronDown}
                  className={`w-3 h-3 transition-transform ${
                    isSalesDropdownOpen ? "rotate-180" : ""
                  }`}
                />
              </button>

              {isSalesDropdownOpen && (
                <ul className="ml-6 space-y-2">
                  <li>
                    <Link
                      to="/Seller"
                      className="block px-4 py-2 text-gray-900 rounded-lg hover:bg-gray-100 dark:text-white dark:hover:bg-gray-700"
                    >
                      Sellers
                    </Link>
                  </li>
                  <li>
                    <Link
                      to="/selle-rorders"
                      className="block px-4 py-2 text-gray-900 rounded-lg hover:bg-gray-100 dark:text-white dark:hover:bg-gray-700"
                    >
                      Seller Order
                    </Link>
                  </li>
                  <li>
                    <Link
                      to="/invoices"
                      className="block px-4 py-2 text-gray-900 rounded-lg hover:bg-gray-100 dark:text-white dark:hover:bg-gray-700"
                    >
                      Invoices
                    </Link>
                  </li>
                  <li>
                    <Link
                      to="/seller-returns"
                      className="block px-4 py-2 text-gray-900 rounded-lg hover:bg-gray-100 dark:text-white dark:hover:bg-gray-700"
                    >
                      Sellers Returns
                    </Link>
                  </li>
                  <li>
                    <Link
                      to="/goods-returns-note"
                      className="block px-4 py-2 text-gray-900 rounded-lg hover:bg-gray-100 dark:text-white dark:hover:bg-gray-700"
                    >
                      Goods Returns Note
                    </Link>
                  </li>
                </ul>
              )}
            </li>
            <li>
              <Link
                to="/sellers"
                className="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group"
              >
                <FontAwesomeIcon
                  icon={faChartBar}
                  className="w-5 h-5 text-gray-500 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white"
                />
                <span className="flex-1 ms-3 whitespace-nowrap">Reports</span>
              </Link>
            </li>
          </ul>
        </div>
      </aside>

      <div className="p-4 sm:ml-64 mt-12">
        <Outlet />
      </div>
    </>
  );
};

export default Dashboard;
